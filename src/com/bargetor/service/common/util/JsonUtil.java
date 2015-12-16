package com.bargetor.service.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bargetor.service.common.util.ReflectUtil.BaseType;
import com.bargetor.service.test.Test;

/**
 * <p>description: Json工具</p>
 * <p>Date: 2013-9-23 上午11:55:45</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class JsonUtil {
	
	/**
	 *<p>Title: beanToJson</p>
	 *<p>Description:对象转换成json对象</p>
	 * @param <T>
	 * @param bean
	 * @return
	 * @return JSONObject 返回类型
	*/
	@SuppressWarnings("unchecked")
	public static <T>JSONObject beanToJson(T bean){
		if(bean == null)return null;
		JSONObject json = new JSONObject();
		Field[] fields = ReflectUtil.getAllFields(bean.getClass());
		if(fields == null)return null;
		for(Field field : fields){
			String name = field.getName();
			Object value = ReflectUtil.getProperty(bean, name);
			try {
				if(ReflectUtil.isBaseType(field)){
					json.put(name, value);
				}else if(ReflectUtil.isCollection(field.getType())){
					JSONArray array = collectionToJSONArray((Collection<Object>) value);
					json.put(name, array);
				}else if(ReflectUtil.isDate(field)){
					if(value != null){
						json.put(name, ((Date)value).getTime());						
					}else{
						json.put(name, (Date)null);
					}
				}else{
					JSONObject subJosn = beanToJson(value);
					json.put(name, subJosn);
				}
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json;
	}
	
	/**
	 * collectionToJSONArray(集合生成JSONArray对象)
	 * (这里描述这个方法适用条件 – 可选)
	 * @param collection
	 * @return
	 *JSONArray
	 * @exception
	 * @since  1.0.0
	*/
	@SuppressWarnings("unchecked")
	public static JSONArray collectionToJSONArray(Collection<Object> collection){
		JSONArray result = new JSONArray();
		if(collection == null || collection.size() <= 0)return result;
		for(Object object : collection){
			if(ReflectUtil.isBaseType(object)){
				result.put(object);
			}else if(ReflectUtil.isCollection(object.getClass())){
				result.put(collectionToJSONArray((Collection<Object>) object));
			}else {
				result.put(beanToJson(object));
			}
		}
		return result;
	}
	
	/**
	 *<p>Title: jsonToBean</p>
	 *<p>Description:把json赋值到对象</p>
	 * @param <T>
	 * @param beanClass
	 * @param json
	 * @return
	 * @return T 返回类型
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws JSONException 
	*/
	@SuppressWarnings("unchecked")
	public static <T>T jsonToBean(Class<T> beanClass,JSONObject json){
		T result = ReflectUtil.newInstance(beanClass);
		return (T) jsonToBean(result, json);
	}
	
	/**
	 *<p>Title: jsonToBean</p>
	 *<p>Description:把json赋值到对象</p>
	 * @param result
	 * @param json
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @return Object 返回类型
	 * @throws JSONException 
	*/
//	public static Object jsonToBean(Object result,JSONObject json) throws InstantiationException, IllegalAccessException{
//		if(result == null || json == null)return null;
//		Iterator<String> it = json.keys();
//		while(it.hasNext()){
//			String key = it.next();
//			try {
//				System.out.println(json.get(key).getClass());
//				ReflectUtil.setProperty(result, key, json.get(key));
//			} catch (JSONException e) {
//				continue;
//			}
//		}
//		return result;
//	}
	
	public static Object jsonToBean(Object result, String jsonStr){
		if (StringUtil.isNullStr(jsonStr))return null;
		JSONObject json = new JSONObject(jsonStr);
		return jsonToBean(result, json);
	}
	
	public static Object jsonToBean(Object result,JSONObject json){
		if(result == null || json == null)return null;
		Field[] fields = ReflectUtil.getAllFields(result.getClass());
		for (Field field : fields) {
			try {
				Object jsonValue = json.get(field.getName());
				
				//这里主要是获取泛型类泛型属性具体类型
				Class<?> realType = ReflectUtil.getFieldRealType(result.getClass(), field);
				
				if(ReflectUtil.isBaseType(realType) && ReflectUtil.isBaseType(jsonValue)){
					ReflectUtil.setProperty(result, field.getName(), jsonValue);
				}else if(ReflectUtil.whichBaseType(realType) == BaseType.String){
					//字符串类型特殊处理,直接将json原始数据写入
					ReflectUtil.setProperty(result, field.getName(), jsonValue.toString());
				}else if(jsonValue instanceof JSONObject && !ReflectUtil.isBaseType(realType)){
					Object value = jsonToBean(realType, (JSONObject)jsonValue);
					ReflectUtil.setProperty(result, field.getName(), value);
				}else if(jsonValue instanceof JSONArray && ReflectUtil.isCollection(realType)){
					Type subType = ReflectUtil.getCollectionActualType(field);
					if(subType == null)continue;
					JSONArray jsonArray = (JSONArray)jsonValue;
					List<Object> value = jsonArrayToCollection(jsonArray, subType);
					ReflectUtil.setProperty(result, field.getName(), value);
				}
				
			} catch (JSONException e) {
				continue;
			}
		}
		return result;
	}
	
	/**
	 * jsonArrayToCollection(将json数组赋值到集合中)
	 * 支持嵌套
	 * @param jsonArray
	 * @param subType subType可以为集合（嵌套） 
	 * @return
	 * List<Object>
	 * @throws JSONException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @exception
	 * @since  1.0.0
	*/
	public static List<Object> jsonArrayToCollection(JSONArray jsonArray, Type subType){
		List<Object> result = null;
		if(!(jsonArray instanceof JSONArray))return result;
		result = new ArrayList<Object>();
		for(int i = 0; i < jsonArray.length(); i++){
			Object subJsonValue = jsonArray.get(i);
			//如果集合是嵌套的
			if(ReflectUtil.isCollection(subType) && subJsonValue instanceof JSONArray){
				List<Object> subListValue = jsonArrayToCollection((JSONArray)subJsonValue, ReflectUtil.getCollectionActualType(subType));
				result.add(subListValue);
			}else {
				Object subValue;
				if(ReflectUtil.isBaseType(subJsonValue)){
					subValue = subJsonValue;
				}else {
					subValue = jsonToBean((Class<?>)subType, (JSONObject)subJsonValue);					
				}
				result.add(subValue);
			}
		}
		return result;
	}
	
	public static <T>T jsonStrToBean(String jsonStr, Class<T> beanClass){
		if (StringUtil.isNullStr(jsonStr))return null;
		JSONObject json = new JSONObject(jsonStr);
		return jsonToBean(beanClass, json);
	}
	
	public static void main(String[] args){
		Test<String> test = new Test<>();
		test.setObj("b");
		Field[] fields = ReflectUtil.getAllFields(test.getClass());
		for(Field field : fields){
			if(field.getName().equals("obj")){
				ReflectUtil.setProperty(test, field.getName(), "c");
			}
			System.out.println(field.getName() + " " +field.getType());
			System.out.println(ReflectUtil.getProperty(test, field.getName()));
		}
	}
}
