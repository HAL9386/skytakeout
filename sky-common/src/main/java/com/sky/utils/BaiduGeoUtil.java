package com.sky.utils;

import com.alibaba.fastjson.JSONObject;
import com.sky.exception.BaiduGeoFailedException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.util.UriUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedHashMap;
import java.util.Map;

@Builder
@AllArgsConstructor
@Data
public class BaiduGeoUtil {
  public static String URL = "https://api.map.baidu.com/geocoding/v3?";
  private String AK;
  private String shopAddress;
  private Double shopLng;
  private Double shopLat;

  /**
   * 计算用户地址与店铺地址之间的地理距离
   * @param userAddress 用户地址
   * @return 两地之间的直线距离（单位：米）
   * @throws Exception 地址解析失败或计算异常时抛出
   */
  public Double calculateDistance(String userAddress) throws Exception {
    Double[] userLngAndLat = getLngAndLat(userAddress);
    Double userLng = userLngAndLat[0];
    Double userLat = userLngAndLat[1];

    // 地球平均半径，单位：米
    final double EARTH_RADIUS = 6371008.8;

    // 将经纬度转换为弧度
    double shopLatRad = Math.toRadians(shopLat);
    double userLatRad = Math.toRadians(userLat);

    // 计算经纬度差值并转换为弧度
    double latDiff = Math.toRadians(userLat - shopLat);
    double lngDiff = Math.toRadians(userLng - shopLng);

    // 使用Haversine公式计算球面距离
    double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
      Math.cos(shopLatRad) * Math.cos(userLatRad) *
        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    // 计算距离（米）

    return EARTH_RADIUS * c;
  }

  /**
   * 通过地址获取经纬度坐标
   *
   * @return 包含经度和纬度的Double数组，[0]=经度(lng)，[1]=纬度(lat)
   */
  public Double[] getLngAndLat(String address) throws Exception {
    Map<String, String> params = new LinkedHashMap<>();
    params.put("address", address);
    params.put("output", "json");
    params.put("ak", AK);

    String jsonResult = requestGetAK(URL, params);

    // 解析JSON字符串
    JSONObject jsonObject = JSONObject.parseObject(jsonResult);
    Integer status = jsonObject.getInteger("status");
    if (status != 0) {
      throw new BaiduGeoFailedException("获取经纬度失败，错误码：" + status + "，错误信息：" + jsonObject.getString("message"));
    }
    JSONObject result = jsonObject.getJSONObject("result");
    JSONObject location = result.getJSONObject("location");

    Double lng = location.getDouble("lng");
    Double lat = location.getDouble("lat");

    return new Double[]{lng, lat};
  }

  /**
   * 默认ak
   * 选择了ak，使用IP白名单校验：
   * 根据您选择的AK已为您生成调用代码
   * 检测到您当前的ak设置了IP白名单校验
   * 您的IP白名单中的IP非公网IP，请设置为公网IP，否则将请求失败
   * 请在IP地址为xxxxxxx的计算发起请求，否则将请求失败
   */
  private String requestGetAK(String strUrl, Map<String, String> param) throws Exception {
    if (strUrl == null || strUrl.isEmpty() || param == null || param.isEmpty()) {
      return null;
    }

    StringBuilder queryString = new StringBuilder();
    queryString.append(strUrl);
    for (Map.Entry<?, ?> pair : param.entrySet()) {
      queryString.append(pair.getKey()).append("=");
      //    第一种方式使用的 jdk 自带的转码方式  第二种方式使用的 spring 的转码方法 两种均可
      //    queryString.append(URLEncoder.encode((String) pair.getValue(), "UTF-8").replace("+", "%20") + "&");
      queryString.append(UriUtils.encode((String) pair.getValue(), "UTF-8")).append("&");
    }

    if (!queryString.isEmpty()) {
      queryString.deleteCharAt(queryString.length() - 1);
    }

    java.net.URL url = new URL(queryString.toString());
//    System.out.println(queryString.toString());
    URLConnection httpConnection = url.openConnection();
    httpConnection.connect();

    InputStreamReader isr = new InputStreamReader(httpConnection.getInputStream());
    BufferedReader reader = new BufferedReader(isr);
    StringBuilder buffer = new StringBuilder();
    String line;
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    reader.close();
    isr.close();
    return buffer.toString();
  }
}
