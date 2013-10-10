package org.gbc.ucitour.net;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import org.apache.commons.io.IOUtils;

/**
 * Dispatches network requests
 */
class NetworkDispatcher {
  private NetworkDispatcher() { }
  
  /**
   * Synchonous get request
   * @param url The url to fetch (no param string)
   * @param params The parameters to attach
   * @return The content of the response
   */
  public static String get(String url, Map<String, String> params) {
    return asyncGet(url, params).get();
  }
  
  /**
   * Synchonous post request
   * @param url The url to fetch
   * @param params The parameters to attach
   * @return The content of the response
   */
  public static String post(String url, Map<String, String> params) {
    return asyncPost(url, params).get();
  }
  
  /**
   * Asynchonous get request
   * @param url The url to fetch (no param string)
   * @param params The parameters to attach
   * @return A Future object containing the response string
   */
  public static Future<String> asyncGet(final String url, final Map<String, String> params) {
    StringBuilder finalUrl = new StringBuilder();
    finalUrl.append(url);
    finalUrl.append('?');
    finalUrl.append(paramString(params));
    return internalFetch(finalUrl.toString(), null);
  }
  
  /**
   * Asynchonous post request
   * @param url The url to fetch
   * @param params The parameters to attach
   * @return A Future object containing the response string
   */
  public static Future<String> asyncPost(final String url, final Map<String, String> params) {
     return internalFetch(url, paramString(params));
  }
  
  private static String paramString(Map<String, String> params) {
    try {
      StringBuilder builder = new StringBuilder();
      for (Map.Entry<String, String> entry : params.entrySet()) {
        builder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        builder.append('=');
        builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        builder.append('&');
      }
      return builder.toString();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
      return null;
    }
  }
  
  private static Future<String> internalFetch(final String url, final String params) {
    final Future<String> future = new Future<String>();
    new Thread(new Runnable() {
      @Override
      public void run() {
        try {
          URL connUrl = new URL(url);
          HttpURLConnection conn = (HttpURLConnection) connUrl.openConnection();
          conn.setDoInput(true);
          if (params != null) {
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", 
                "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", "" + 
                    Integer.toString(params.getBytes().length));
            conn.setRequestProperty("Content-Language", "en-US");  
            conn.setUseCaches (false);
            OutputStream output = conn.getOutputStream();
            output.write(params.getBytes());
            output.flush();
            output.close();
          } else {
            conn.setRequestMethod("GET");
          }
          future.set(new String(IOUtils.toByteArray(conn.getInputStream())));
        } catch (IOException e) {
          future.set(null);
          e.printStackTrace();
        }
      }
    }).start();
    return future;
  }
}
