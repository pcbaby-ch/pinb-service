package com.pinb.util;
/**
 *
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.cglib.beans.BeanMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Map2Bean Bean2Map 相互转换
 * 
 * @author chen.zhao (chenzhao) @DATE: 2018年3月2日
 */
public class MapBeanUtil {
	/**
	 * 将对象装换为map
	 * 
	 * @param bean
	 * @return
	 */
	public static <T> Map<String, Object> beanToMap(T bean) {
		Map<String, Object> map = Maps.newHashMap();
		if (bean != null) {
			BeanMap beanMap = BeanMap.create(bean);
			for (Object key : beanMap.keySet()) {
				map.put(key + "", beanMap.get(key));
			}
		}
		return map;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> Map<String, String> beanToMapStr(T bean) {
		Map<String, Object> srcMap = beanToMap(bean);
		Map<String, String> map = new HashMap<>();
		for (Iterator iterator = srcMap.entrySet().iterator(); iterator.hasNext();) {
			Entry<String, Object> entry = (Entry<String, Object>) iterator.next();
			map.put(entry.getKey(), entry.getValue() + "");
		}
		return map;
	}

	/**
	 * 将map装换为javabean对象
	 * 
	 * @param map
	 * @param bean
	 * @return
	 */
	public static <T> T mapToBean(Map<String, Object> map, T bean) {
		BeanMap beanMap = BeanMap.create(bean);
		beanMap.putAll(map);
		return bean;
	}

	/**
	 * 将List<T>转换为List<Map<String, Object>>
	 * 
	 * @param objList
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> List<Map<String, Object>> objectsToMaps(List<T> objList) {
		List<Map<String, Object>> list = Lists.newArrayList();
		if (objList != null && objList.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = objList.size(); i < size; i++) {
				bean = objList.get(i);
				map = beanToMap(bean);
				list.add(map);
			}
		}
		return list;
	}

	/**
	 * 将List<T>转换为Set<T> ,会过滤重复数据(**小心使用，可能丢失数据**)
	 * 
	 * @param objList
	 * @return
	 * @throws JsonGenerationException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static <T> Set<Object> objectsToSet(List<T> objList, String fieldKey) {
		Set<Object> set = new HashSet<>();
		if (objList != null && objList.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = objList.size(); i < size; i++) {
				bean = objList.get(i);
				map = beanToMap(bean);
				set.add(map.get(fieldKey));
			}
		}
		return set;
	}

	/**
	 * 将set转为，分隔的字符串
	 * 
	 * @author chenzhao @date May 16, 2019
	 * @param set
	 * @return
	 */
	public static String setToStrs(Set set) {
		StringBuffer strb = new StringBuffer();
		for (Iterator iterator = set.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			strb.append("'").append(object).append("',");
		}
		return strb.substring(0, strb.lastIndexOf(","));
	}

	/**
	 * 将对象list转为对象某个字段为的Map，转为map方便索引对象，--将因keyfield而去重(**小心使用，可能丢失数据**)
	 * 
	 * @param objList
	 * @param key     对象中的字段，
	 * @return
	 */
	public static <T> Map<String, Object> objListToMap(List<T> objList, String fieldKey) {
		Map<String, Object> map = new HashMap();
		if (objList != null && objList.size() > 0) {
			Map<String, Object> mapTemp = null;
			T bean = null;
			for (int i = 0, size = objList.size(); i < size; i++) {
				bean = objList.get(i);
				mapTemp = beanToMap(bean);
				map.put(mapTemp.get(fieldKey) + "", bean);
			}
		}
		return map;
	}

	/**
	 * 将对象map转为对象list，方便输出
	 * 
	 * @param map
	 * @return
	 */
	public static <T> List<Object> map2ObjList(Map<String, T> map) {
		List<Object> list = Lists.newArrayList();
		if (map != null && map.size() > 0) {
			T bean = null;
			for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext();) {
				Entry<String, T> entry = (Entry<String, T>) iterator.next();
				list.add(entry.getValue());
			}
		}
		return list;
	}

	/**
	 * 将List<Map<String,Object>>转换为List<T>
	 * 
	 * @param maps
	 * @param clazz
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public static <T> List<T> mapsToObjects(List<Map<String, Object>> maps, Class<T> clazz)
			throws InstantiationException, IllegalAccessException {
		List<T> list = Lists.newArrayList();
		if (maps != null && maps.size() > 0) {
			Map<String, Object> map = null;
			T bean = null;
			for (int i = 0, size = maps.size(); i < size; i++) {
				map = maps.get(i);
				bean = clazz.newInstance();
				mapToBean(map, bean);
				list.add(bean);
			}
		}
		return list;
	}
}
