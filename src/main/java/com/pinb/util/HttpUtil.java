package com.pinb.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http访问
 * 
 * @author chenzhao @date Apr 12, 2019
 */
public class HttpUtil {

	private static final Logger log = LoggerFactory.getLogger(HttpUtil.class);

	public static String doPost(String url, String body, String[][] headers, int connectionTimeout, int readTimeout,
			String charset) {
		long reqstart = System.currentTimeMillis();
		log.info(">>>reqURL:[{}],#reqBody:[{}]", url, body);
		URL uri = null;
		HttpURLConnection urlConnection = null;
		try {
			uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(1000 * connectionTimeout); // 连接超时时间
			urlConnection.setReadTimeout(1000 * readTimeout); // 响应超时时间
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);

			if (null == headers) {
				urlConnection.addRequestProperty("Content-Type", "application/json");
			} else {
				// 设置Http报文请求头
				for (String[] header : headers) {
					urlConnection.addRequestProperty(header[0], header[1]);
				}
			}

			// 提交Http请求参数
			urlConnection.connect();
			if (null != body) {
				urlConnection.getOutputStream().write(body.getBytes(charset));
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}

			// 读取响应参数
			int responseCode = urlConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				InputStream is = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
				StringBuffer response = new StringBuffer();
				String line = br.readLine();
				while (null != line) {
					response.append(line);
					line = br.readLine();
				}
				br.close();
				is.close();
				log.info(">>>resp:[{}],#interval:[{}]", response, System.currentTimeMillis() - reqstart);
				return response.toString();
			} else {
				log.info("#doPost({}) 请求失败!{}", url, responseCode);
			}
		} catch (Exception e) {
			log.error("#doPost({}) 请求异常:{}", url, e.toString(), e);
		} finally {
			if (null != urlConnection) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	public static String doPost(String url, String body) {
		long reqstart = System.currentTimeMillis();
		log.info(">>>reqURL:[{}],#reqBody:[{}]", url, body);
		URL uri = null;
		HttpURLConnection urlConnection = null;
		try {
			uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(1000 * 3); // 连接超时时间
			urlConnection.setReadTimeout(1000 * 8); // 响应超时时间
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);

			urlConnection.addRequestProperty("Content-Type", "application/json");

			// 提交Http请求参数
			urlConnection.connect();
			if (null != body) {
				urlConnection.getOutputStream().write(body.getBytes("UTF-8"));
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}

			// 读取响应参数
			int responseCode = urlConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				InputStream is = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				StringBuffer response = new StringBuffer();
				String line = br.readLine();
				while (null != line) {
					response.append(line);
					line = br.readLine();
				}
				br.close();
				is.close();
				log.info(">>>resp:[{}],#interval:[{}]", response, System.currentTimeMillis() - reqstart);
				return response.toString();
			} else {
				log.info("#doPost({}) 请求失败!{}", url, responseCode);
			}
		} catch (Exception e) {
			log.error("#doPost({}) 请求异常:{}", url, e.toString(), e);
		} finally {
			if (null != urlConnection) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	public static Object doPost4Buffer(String url, String body) {
		long reqstart = System.currentTimeMillis();
		log.info(">>>reqURL:[{}],#reqBody:[{}]", url, body);
		URL uri = null;
		HttpURLConnection urlConnection = null;
		try {
			uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setRequestMethod("POST");
			urlConnection.setConnectTimeout(1000 * 3); // 连接超时时间
			urlConnection.setReadTimeout(1000 * 8); // 响应超时时间
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setUseCaches(false);

			urlConnection.addRequestProperty("Content-Type", "application/json");

			// 提交Http请求参数
			urlConnection.connect();
			if (null != body) {
				urlConnection.getOutputStream().write(body.getBytes("UTF-8"));
				urlConnection.getOutputStream().flush();
				urlConnection.getOutputStream().close();
			}

			// 读取响应参数
			int responseCode = urlConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				InputStream is = urlConnection.getInputStream();
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				// 创建一个Buffer字符串
				byte[] buffer = new byte[1024];
				// 每次读取的字符串长度，如果为-1，代表全部读取完毕
				int len = 0;
				// 使用一个输入流从buffer里把数据读取出来
				while ((len = is.read(buffer)) != -1) {
					// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
					outStream.write(buffer, 0, len);
				}
				// 关闭输入流
				is.close();
				// 把outStream里的数据写入内存
				if (outStream.toByteArray().length > 0) {// #流数据读取成功
					log.info(">>>resp:[字节流数据],#interval:[{}]", System.currentTimeMillis() - reqstart);
					return outStream.toByteArray();
				} else {
					BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
					StringBuffer response = new StringBuffer();
					String line = br.readLine();
					while (null != line) {
						response.append(line);
						line = br.readLine();
					}
					br.close();
					is.close();
					log.info(">>>resp:[{}],#interval:[{}]", response, System.currentTimeMillis() - reqstart);
					return response.toString();
				}
			} else {
				log.info("#doPost({}) 请求失败!{}", url, responseCode);
			}
		} catch (Exception e) {
			log.error("#doPost({}) 请求异常:{}", url, e.toString(), e);
		} finally {
			if (null != urlConnection) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	public static String doGet(String url, String[][] headers, int connectionTimeout, int readTimeout, String charset) {
		long reqstart = System.currentTimeMillis();
		log.info(">>>reqURL:[{}]", url);
		URL uri = null;
		HttpURLConnection urlConnection = null;
		try {
			uri = new URL(url);
			urlConnection = (HttpURLConnection) uri.openConnection();
			urlConnection.setConnectTimeout(1000 * connectionTimeout);
			urlConnection.setReadTimeout(1000 * readTimeout);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setUseCaches(false);
			if (headers != null) {
				for (String[] header : headers) {
					urlConnection.addRequestProperty(header[0], header[1]);
				}
			}
			urlConnection.connect();
			// 读取响应参数
			int responseCode = urlConnection.getResponseCode();
			if (HttpURLConnection.HTTP_OK == responseCode) {
				InputStream is = urlConnection.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
				StringBuffer response = new StringBuffer();
				String line = br.readLine();
				while (null != line) {
					response.append(line);
					line = br.readLine();
				}
				br.close();
				is.close();
				log.info(">>>resp:[{}],#interval:[{}]", response, System.currentTimeMillis() - reqstart);
				return response.toString();
			} else {
				log.info("#doPost({}) 请求失败!{}", url, responseCode);
			}
		} catch (Exception e) {
			log.error("#doPost({}) 请求异常:{}", url, e.toString(), e);
		} finally {
			if (null != urlConnection) {
				urlConnection.disconnect();
			}
		}
		return null;
	}

	/**
	 * 
	 * @param url
	 * @param reqMap 为空时，则无参get请求
	 * @return
	 */
	public static String doGet(String url, HashMap<String, Object> reqMap) {
		if (reqMap == null || reqMap.isEmpty()) {
			return doGet(url, null, 3, 8, "utf-8");
		}
		return doGet(url + "?" + getUrlParams(reqMap), null, 3, 8, "utf-8");
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static String getUrlParams(HashMap<String, Object> reqMap) {
		StringBuffer respbuf = new StringBuffer();
		for (Iterator iterator = reqMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
			respbuf.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		return respbuf.substring(1, respbuf.length());
	}

}
