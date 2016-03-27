/**
 * bargetorCommon
 * com.bargetor.nest.test
 * Test.java
 * 
 * 2015年3月4日-下午1:54:02
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.test;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.bargetor.nest.common.http.HttpResponse;
import com.bargetor.nest.common.util.ReflectUtil;

/**
 *
 * Test
 * 
 * kin kin 2015年3月4日 下午1:54:02
 * 
 * @version 1.0.0
 *
 */
public class Test<T> extends ClassA<T> {
	private List<List<String>> list;

	private Map<String, Object> map;

	public void testA() throws NoSuchFieldException, SecurityException {
		Field field = Test.class.getDeclaredField("list");
		System.out.println(field.getType().isAssignableFrom(Collection.class));
		System.out.println(Collection.class.isAssignableFrom(field.getType()));
		for (Type t1 : ReflectUtil.getFieldActualTypeArguments(field)) {
			System.out.println(t1 + ",");
		}
	}

	public static void main(String args[]) throws Exception {
		Test<HttpResponse> test = new Test<HttpResponse>();
		test.testA();
		System.out.println("======getSuperclass======:");
		System.out.println(Test.class.getSuperclass().getName());
		System.out.println("======getGenericSuperclass======:");
		Type t = Test.class.getGenericSuperclass();
		System.out.println(t);
		if (ParameterizedType.class.isAssignableFrom(t.getClass())) {
			System.out.print("----------->getActualTypeArguments:");
			for (Type t1 : ReflectUtil.getTypeActualTypeArguments(t)) {
				System.out.print(t1 + ",");
			}
			System.out.println();
		}
	}

	/**
	 * list
	 *
	 * @return  the list
	 * @since   1.0.0
	*/
	
	public List<List<String>> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<List<String>> list) {
		this.list = list;
	}

	/**
	 * map
	 *
	 * @return  the map
	 * @since   1.0.0
	*/
	
	public Map<String, Object> getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(Map<String, Object> map) {
		this.map = map;
	}
	
}
