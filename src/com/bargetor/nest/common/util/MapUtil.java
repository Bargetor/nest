/**
 * Migrant
 * com.bargetor.migrant.util
 * MapUtil.java
 * 
 * 2015年2月8日-下午8:55:54
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.util;

import java.lang.reflect.Field;
import java.util.*;
import java.util.Map.Entry;

/**
 *
 * MapUtil
 * 
 * kin kin 2015年2月8日 下午8:55:54
 * 
 * @version 1.0.0
 *
 */
public class MapUtil {

	/**
	 * sortMap(map排序)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param oriMap
	 * @param comparator
	 * @return
	 *Map<T,N>
	 * @exception
	 * @since  1.0.0
	*/
	public static <T, N>Map<T, N> sortMap(Map<T, N> oriMap, Comparator<Entry<T, N>> comparator) {
		Map<T, N> sortedMap = new LinkedHashMap<T, N>();
		if (oriMap != null && !oriMap.isEmpty()) {
			List<Entry<T, N>> entryList = new ArrayList<Map.Entry<T, N>>(oriMap.entrySet());
			Collections.sort(entryList, comparator);
			Iterator<Map.Entry<T, N>> iter = entryList.iterator();
			Map.Entry<T, N> tmpEntry = null;
			while (iter.hasNext()) {
				tmpEntry = iter.next();
				sortedMap.put(tmpEntry.getKey(), tmpEntry.getValue());
			}
		}
		return sortedMap;
	}
	
	/**
	 * beanToParamsMap(将bean转化为参数map,不支持嵌套)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param bean
	 * @return
	 *Map<String,Object>
	 * @exception
	 * @since  1.0.0
	*/
	public static Map<String, String> beanToParamsMap(Object bean){
		if(bean == null)return null;
		Map<String, String> result = new LinkedHashMap<String, String>();
		Field[] fields = bean.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object value = ReflectUtil.getProperty(bean, field.getName());
			if(value == null) continue;
			result.put(field.getName(), value.toString());
		}
		return result;
	}
	
	/**
	 * mapToBean(将map转化为bean,不支持嵌套)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param map
	 * @param beanClass
	 * @return
	 * T
	 * @exception
	 * @since  1.0.0
	*/
	public static <T> T mapToBean(Map<String, String> map, Class<T> beanClass){
		if(map == null || map.size() <= 0)return null;
		T result = ReflectUtil.newInstance(beanClass);
		Field[] fields = beanClass.getDeclaredFields();
		for (Field field : fields) {
			String value = map.get(field.getName());
			if(value == null) continue;
			ReflectUtil.setProperty(result, field.getName(), value);
		}
		return result;
	}
	
	/**
	 * concatParams(url params 形式拼接)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param params
	 * @return
	 * String
	 * @exception
	 * @since  1.0.0
	*/
	public static String concatParams(Map<String, String> params){
		if(params == null || params.size() <= 0)return null;
		List<String> pairs = new ArrayList<String>();
		for(Entry<String, String> entry : params.entrySet()){
			String key = entry.getKey();
			String value = entry.getValue();
			pairs.add(key + "=" + value);
		}
		
		return StringUtil.joinList(pairs, "&");
	}

	public static boolean isMapNull(Map<?, ?> map){
		return map == null || map.size() <= 0;
	}

	public static boolean isMapNotNull(Map<?, ?> map){
		return !isMapNull(map);
	}

	public static <K, V> Map<K, V>list2Map(List<V> list, GetKey<K, V> getKey){
		if(list == null || list.size() <= 0)return null;
		if(getKey == null)return null;
		Map<K, V> result = new HashMap<>();
		for (V v : list) {
			K key = getKey.getKey(v);
			if(key == null)continue;
			result.put(key, v);
		}
		return result;
	}

	public interface GetKey<K, V>{
		public K getKey(V entry);
	}
		
}
