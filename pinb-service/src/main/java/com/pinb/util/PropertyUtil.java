package com.pinb.util;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

/**
 * 配置文件功能处理类
 * @author chenzhao @date Apr 12, 2019
 */
public class PropertyUtil {
	
	private static final Logger LOG = Logger.getLogger(PropertyUtil.class);
	private static final String PROPERTIES_SUFFIX = ".properties";
	private static List<String> register = Collections.synchronizedList(new ArrayList<String>());
	private static Map<String, String> keyValues = new ConcurrentHashMap<>();
	private static Timer timer = new Timer(PropertyUtil.class.getName());
	private static final long DELAY_TIME = 5000; // 延迟加载时间(单位：ms)
	
	/**
	 * 获取文件绝对路径
	 * @author chenzhao @date Apr 12, 2019
	 * @return
	 */
	private static String getClassPath(){
		return PropertyUtil.class.getResource("/").getPath();
	}
	
	/**
	 * ********************************************
	 * method name   : getFileAboslutePath 
	 * description   : 获取文件绝对路径
	 * @return       : String
	 * @param        : @param filename
	 * @param        : @return
	 * modified      : hejinyun@umpay.com ,  2016-11-10  上午9:52:02
	 * @see          : 
	 * *******************************************
	 */
	private static String getFileAboslutePath(String filename){
		StringBuffer buffer = new StringBuffer();
		buffer.append(getClassPath());
		buffer.append(filename);
		buffer.append(PROPERTIES_SUFFIX);
		return buffer.toString();
	}
	
	/**
	 * 配置文件注册
	 * @author chenzhao @date Apr 12, 2019
	 * @param filenames
	 */
	public static void register(String... filenames){
		for (String filename : filenames){
			if (!register.contains(filename)){
				register.add(filename);
				final TimerTask task = new PropertiesTask(filename);
				timer.schedule(task, 0, DELAY_TIME);
			}
		}
	}
	
	private static void loadProperties(String filename){
		InputStream is = null;
		try {
			String fileAbsolutePath = getFileAboslutePath(filename);
			is = new FileInputStream(fileAbsolutePath);
			Properties properties = new Properties();
			properties.load(is);
			is.close();
			if (!properties.isEmpty()){
				Set<Entry<Object, Object>> set = properties.entrySet();
				Iterator<Map.Entry<Object, Object>> it = set.iterator();
				String key = null;
				String value = null;
				while(it.hasNext()){
					Entry<Object, Object> entry = it.next();
					key = String.valueOf(entry.getKey());
					value = String.valueOf(entry.getValue());
					keyValues.put(key, value);
				}
				LOG.debug("#loadProperties() Properties文件内容:"+keyValues);
			}
			
		} catch (Exception e){
			LOG.error("#loadProperties() Properties文件加载异常", e);
		}
	}
	
	public static String getStrValue(String key, String defaultValue){
		String value = keyValues.get(key);
		return (null == value)?defaultValue:value.trim();
	}
	
	public static Integer getIntValue(String key, Integer defaultValue){
		String value = keyValues.get(key);
		return (null == value)?defaultValue:Integer.valueOf(value.trim());
	}
	
	static class PropertiesTask extends TimerTask{
		
		private String filename;
		private long lastModified;
		
		public PropertiesTask(String filename){
			super();
			this.filename = filename;
			this.lastModified = 0l;
		}
		
		public void run() {
			try {
				File file = new File(getFileAboslutePath(filename));
				if (!file.exists()){
					LOG.info(filename+"文件不存在!");
				}
				long newLastModified = file.lastModified();
				if (newLastModified > lastModified){
					LOG.info("Properties文件["+filename+"]有变动,重新加载!");
					lastModified = newLastModified;
					loadProperties(filename);
				}
			} catch (Exception e){
				LOG.error("#run() Properties文件处理异常", e);
			}
		}
	}
	
	public static void main(String[] args) throws Exception{
		PropertyUtil.register("system", "test");
		Thread.sleep(200l);
		System.out.println(PropertyUtil.getStrValue("platform.name", "Vstore-business"));
	}
}




