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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

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

	public static <K, V>Map<K, V> randomEntry(Map<K, V> map, int count){
		if(isMapNull(map))return null;
		if(count <= 0)return null;
		if(count >= map.size())return new HashMap<>(map);

		List<K> randomKeys = ArrayUtil.randomValue(map.keySet(), count);
		return subMap(map, randomKeys);
	}

	public static <K, V>Map<K, V> subMap(Map<K, V> map, List<K> subKeys){
		if(isMapNull(map) || ArrayUtil.isNull(subKeys))return null;
		try {
			Map<K, V> subMap = map.getClass().newInstance();

			subKeys.forEach(key -> {
				subMap.put(key, map.get(key));
			});

			return subMap;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

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
			Iterator<Map.Entry<T, N>> iterator = entryList.iterator();
			Map.Entry<T, N> tmpEntry = null;
			while (iterator.hasNext()) {
				tmpEntry = iterator.next();
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
		Field[] fields = ReflectUtil.getAllFields(bean.getClass());
		for (Field field : fields) {
			Object value = ReflectUtil.getProperty(bean, field);
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

	public static <OK, OV, NK, NV> Map<NK, NV>map2Map(Map<OK, OV> map, BiTransformer<OK, OV, NK, NV> transformer){
		if(isMapNull(map))return null;
		if(transformer == null)return null;
		Map<NK, NV> result = ReflectUtil.newInstance(map.getClass());

		map.forEach((key, value) -> {
			SimpleMapEntry<NK, NV> entry = transformer.transform(key, value);
			result.put(entry.getKey(), entry.getValue());
		});

		return result;
	}

	public interface BiTransformer<OK, OV, NK, NV>{
		SimpleMapEntry<NK, NV> transform(OK key, OV value);
	}

	public static class SimpleMapEntry<K, V>{
		private K key;
		private V value;

		public SimpleMapEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public void setKey(K key) {
			this.key = key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}
	}


	public static <K, V>Map<K, V> filter(Map<K, V> map, MapFilter<K, V> filter){
		if(isMapNull(map))return null;
		if(filter == null)return new HashMap<>(map);

		Map<K, V> filterC = new ConcurrentHashMap<>();

		map.forEach((key, value) -> {
			if(value == null)return;
			if(filter.filter(key, value))filterC.put(key, value);
		});

		return filterC;
	}

	public interface  MapFilter<K, V>{
		boolean filter(K key, V value);
	}

	public static <K, V>void remove(Map<K, V> map, Collection<K> keys){
		if(isMapNull(map) || ArrayUtil.isNull(keys))return;

		keys.forEach(key -> {
			map.remove(key);
		});
	}
}
