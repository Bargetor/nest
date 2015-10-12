package com.bargetor.service.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.NotSerializableException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Blob;
import java.sql.Clob;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * <p>description: 对Object进行深度克隆</p>
 * <p>Date: Feb 3, 2012 11:38:01 AM</p>
 * <p>modify：</p>
 * @author: majin
 * @version: 1.0
 * </p>Company: 北京合力金桥软件技术有限责任公司</p>
 * <p>对一个javaBean对象进行深度克隆</p>
 * <p>优先级别为,过滤器->基本类型->特殊对象克隆->序列化克隆->调用自身实现的clone方法->递归克隆->null</p>
 */
@SuppressWarnings("unchecked")
public class ObjectClone {
	/** 过滤器 */
	private ObjectClone.cloneFilter filter;
	private String model;
	
    /**
     *<p>Title: deepClone</p>
     *<p>Description:利用递归调用对对象进行深层克隆</p>
     * @param @param obj
     * @param @return
     * @param @throws Exception 设定文件
     * @return  Object 返回类型
     * @throws
    */
	
    public <T>T deepClone(T obj) throws Exception {
    	if(obj == null) return null;
    	//过滤器
    	if(this.filter != null && !this.filter.filter(obj)){
    		if(this.model == null) return null;
    		if(ObjectClone.cloneFilter.IMPORT_FILTER_RETURN.equals(this.model))return obj;
    		return null;    		
    	}
    	//如果是基础类型，直接返回
    	if(ReflectUtil.isBaseType(obj))return obj;
    	
    	//特殊对象克隆
    	ObjectCloneForSpecial cloneForSpecial = new ObjectCloneForSpecial();
    	T newObjForSpecial = cloneForSpecial.cloneForSpecial(obj);
    	if(newObjForSpecial != null) return newObjForSpecial;
    	
    	//进行序列化克隆
    	T newObjByStrem = cloneByStrem(obj);
    	if(newObjByStrem != null) return newObjByStrem;
    	
    	//调用自身实现克隆函数
    	Method cloneMethod = ReflectUtil.getMethod(obj.getClass(), "clone", new Class[]{});
    	//如果对象实现了Cloneable的clone方法，则调用该方法
    	if(cloneMethod != null && ReflectUtil.isInterfaceToAchieve(obj.getClass(), "Cloneable")){
    		T newObjectByClone = (T) cloneMethod.invoke(obj, new Object[]{});
    		return newObjectByClone;
    	}
    	
    	//一般JavaBean对象克隆
		// getDeclaredFields得到object内定义的所有field
        Field[] fields = obj.getClass().getDeclaredFields();
		// 利用newInstance方法，生成一个空的Object
        T newObj = (T) obj.getClass().newInstance();
        for (int i = 0, j = fields.length; i < j; i++) {
			String propertyName = fields[i].getName();
			// field的值
			Object propertyValue = ReflectUtil.getProperty(obj, propertyName);
			if (propertyValue != null) {
				// 如果field不是8种基本类型，或者String，则直接赋值
				if (ReflectUtil.isBaseType(propertyValue)) {
					ReflectUtil.setProperty(newObj, propertyName, propertyValue);
				} else {
					// 如果field类型是其他Object且没有实现clone方法，则递归克隆
					Object newPropObj = deepClone(propertyValue);
					ReflectUtil.setProperty(newObj, propertyName, newPropObj);
				}
			}
		}
        return newObj;
    }
    
    /**
     *<p>Title: deepClone</p>
     *<p>Description:带过滤器的深度克隆</p>
     * @param @param <T>
     * @param @param obj
     * @param @param filter
     * @param @return
     * @param @throws Exception 设定文件
     * @return  T 返回类型
     * @throws
    */
    public <T>T deepClone(T obj,ObjectClone.cloneFilter filter) throws Exception{
    	this.filter = filter;
    	return deepClone(obj);
    }
    
    /**
     *<p>Title: deepClone</p>
     *<p>Description:带模式，带过滤器的深度克隆</p>
     * @param @param <T>
     * @param @param obj
     * @param @param filter
     * @param @param model
     * @param @return
     * @param @throws Exception 设定文件
     * @return  T 返回类型
     * @throws
    */
    public <T>T deepClone (T obj,String model,ObjectClone.cloneFilter filter) throws Exception{
    	this.model = model;
    	return deepClone(obj,filter);
    }

    /**
     *<p>Title: basicClone</p>
     *<p>Description:只对对象进行特殊对象克隆和一般克隆</p>
     * @param @param <T>
     * @param @param obj
     * @param @return
     * @param @throws Exception 设定文件
     * @return  T 返回类型
     * @throws
    */
    public <T>T basicClone(T obj) throws Exception{
    	if(obj == null) return null;
    	//过滤器
    	if(this.filter != null && !this.filter.filter(obj)){
    		if(this.model == null) return null;
    		if(ObjectClone.cloneFilter.IMPORT_FILTER_RETURN.equals(this.model))return obj;
    		return null;    		
    	}
    	//如果是基础类型，直接返回
    	if(ReflectUtil.isBaseType(obj))return obj;
    	
    	//特殊对象克隆
    	ObjectCloneForSpecial cloneForSpecial = new ObjectCloneForSpecial();
    	T newObjForSpecial = cloneForSpecial.cloneForSpecial(obj);
    	if(newObjForSpecial != null) return newObjForSpecial;
    	
    	//一般JavaBean对象克隆
		// getDeclaredFields得到object内定义的所有field
        Field[] fields = obj.getClass().getDeclaredFields();
		// 利用newInstance方法，生成一个空的Object
        T newObj = (T) obj.getClass().newInstance();
        for (int i = 0, j = fields.length; i < j; i++) {
			String propertyName = fields[i].getName();
			// field的值
			Object propertyValue = ReflectUtil.getProperty(obj, propertyName);
			if (propertyValue != null) {
				// 如果field不是8种基本类型，或者String，则直接赋值
				if (ReflectUtil.isBaseType(propertyValue)) {
					ReflectUtil.setProperty(newObj, propertyName, propertyValue);
				} else {
					// 如果field类型是其他Object且没有实现clone方法，则递归克隆
					Object newPropObj = basicClone(propertyValue);
					ReflectUtil.setProperty(newObj, propertyName, newPropObj);
				}
			}
		}
        return newObj;
    }
    
    /**
     *<p>Title: basicClone</p>
     *<p>Description:带过滤器的深度克隆</p>
     * @param @param <T>
     * @param @param obj
     * @param @param filter
     * @param @return
     * @param @throws Exception 设定文件
     * @return  T 返回类型
     * @throws
    */
    public <T>T basicClone(T obj,ObjectClone.cloneFilter filter) throws Exception{
    	this.filter = filter;
    	return basicClone(obj);
    }
    
    /**
     *<p>Title: basicClone</p>
     *<p>Description:带模式，带过滤器的深度克隆</p>
     * @param @param <T>
     * @param @param obj
     * @param @param filter
     * @param @param model
     * @param @return
     * @param @throws Exception 设定文件
     * @return  T 返回类型
     * @throws
    */
    public <T>T basicClone (T obj,String model,ObjectClone.cloneFilter filter) throws Exception{
    	this.model = model;
    	return basicClone(obj,filter);
    }
    /**
     *<p>Title: clone</p>
     *<p>Description:对象浅度克隆</p>
     * @param @param obj
     * @param @return
     * @param @throws Exception 设定文件
     * @return  Object 返回类型
     * @throws
    */
    public <T>T clone(T obj) throws Exception {
    	if(ReflectUtil.isBaseType(obj)) return obj;
        Field[] fields = obj.getClass().getDeclaredFields();
        T newObj = (T) obj.getClass().newInstance();
        for (int i = 0, j = fields.length; i < j; i++) {
            String propertyName = fields[i].getName();
            Object propertyValue = ReflectUtil.getProperty(obj, propertyName);
            ReflectUtil.setProperty(newObj, propertyName, propertyValue);
        }
        return newObj;
    }
    
    /**
     *<p>Title: getterToSetter</p>
     *<p>Description:把数据从getter转移到setter,不为基础数据的则深度克隆</p>
     * @param @param <T>
     * @param @param getter
     * @param @param setter
     * @param @return 设定文件
     * @return  T 返回类型
     * @throws Exception 
     * @throws
    */
    public <T,V>void getterToSetter(T getter,V setter) throws Exception{
    	if(getter == null || setter == null) return;
    	Field[] fields = getter.getClass().getDeclaredFields();
    	for(int i = 0,len = fields.length;i<len;i++){
    		Object thisFieldValue = ReflectUtil.getProperty(getter, fields[i].getName());
    		if(thisFieldValue == null)continue;
    		thisFieldValue = deepClone(thisFieldValue);
    		ReflectUtil.setProperty(setter, fields[i].getName(), thisFieldValue);
    	}
    }
    
    /**
     *<p>Title: getterToSetter</p>
     *<p>Description:把数据从getter转移到setter,只做基础数据转移(包括Date)</p>
     * @param @param <T>
     * @param @param getter
     * @param @param setter
     * @param @return 设定文件
     * @return  T 返回类型
     * @throws Exception 
     * @throws
    */
    public static <T, V>void getterToSetterForBase(T getter,V setter){
    	if(getter == null || setter == null) return;
    	Field[] fields = getter.getClass().getDeclaredFields();
    	for(int i = 0,len = fields.length;i<len;i++){
    		Field field = fields[i];
    		Object thisFieldValue = ReflectUtil.getProperty(getter, field.getName());
    		if(thisFieldValue == null)continue;
    		if(!ReflectUtil.isBaseType(thisFieldValue) && !ReflectUtil.isDate(field))continue;
    		ReflectUtil.setProperty(setter, fields[i].getName(), thisFieldValue);
    	}
    }
    
    /**
     *<p>Title: getterToSetterByMethod</p>
     *<p>Description:把数据从getter转移到setter,不为基础数据的则深度克隆</p>
     * @param @param <T>
     * @param @param <V>
     * @param @param getter
     * @param @param setter
     * @param @throws Exception 设定文件
     * @return  void 返回类型
     * @throws
    */
    public <T,V>void getterToSetterByMethod(T getter,V setter) throws Exception{
    	if(getter == null || setter == null) return;
    	Method[] methods = getter.getClass().getMethods();
    	for(Method method : methods){
    		if(method.getParameterTypes().length > 0) continue;
    		Method set = ReflectUtil.getMethod(setter.getClass(), "set"+method.getName().substring(3), new Class[]{method.getReturnType()});
    		if(set == null)continue;
    		Object thisFieldValue = method.invoke(getter, new Object[]{});
    		if(thisFieldValue==null)continue;
    		thisFieldValue = deepClone(thisFieldValue);
    		set.invoke(setter, thisFieldValue);
    	}
    }
      
    /**
     *<p>Title: cloneByStrem</p>
     *<p>Description:对对象进行序列化克隆</p>
     * @param @param src
     * @param @return 设定文件
     * @return  Object 返回类型
     * @throws
    */
	private <T>T cloneByStrem(T src) {
		if(src == null)return null;
		if(!ReflectUtil.isInterfaceToAchieve(src.getClass(), "Serializable"))return null;
        T dst = null;
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(out);
            oo.writeObject(src);

            ByteArrayInputStream in = new ByteArrayInputStream(
                    out.toByteArray());
            ObjectInputStream oi = new ObjectInputStream(in);
            dst = (T) oi.readObject();
            return  dst;
        } catch (NotSerializableException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } catch (IOException e) {
			return null;
		}
    }

    
/****************************************** ObjectCloneForSpecial ********************************************************/
    /**
     * <p>description: 特殊对象的克隆</p>
     * <p>Date: Feb 3, 2012 2:01:51 PM</p>
     * <p>modify：</p>
     * @author: majin
     * @version: 1.0
     * </p>Company: 北京合力金桥软件技术有限责任公司</p>
     */
    private class ObjectCloneForSpecial {
    	
    	/**
    	 *<p>Title: cloneForMap</p>
    	 *<p>Description:对Map进行克隆</p>
    	 * @param @param <T>
    	 * @param @param <V>
    	 * @param @param map
    	 * @param @return
    	 * @param @throws Exception 设定文件
    	 * @return  Map<T,V> 返回类型
    	 * @throws
    	*/
    	public <T,V>Map<T,V> cloneForMap(Map<T,V> map) throws Exception{
    		if(map == null) return null;
    		Map<T,V> newMap = map.getClass().newInstance();
    		if(map.isEmpty()) return newMap;
    		Set<T> keySet = map.keySet();
    		for(T key:keySet){
    			T newKey = (T) deepClone(key);
    			V newValue = (V) deepClone(map.get(key));
    			newMap.put(newKey, newValue);
    		}
    		return newMap;
    	}
    	
    	/**
    	 *<p>Title: cloneForCollection</p>
    	 *<p>Description:对集合进行克隆</p>
    	 * @param @param <T>
    	 * @param @param collection
    	 * @param @return
    	 * @param @throws Exception 设定文件
    	 * @return  Collection 返回类型
    	 * @throws
    	*/
    	public <T>Collection<T> cloneForCollection (Collection<T> collection) throws Exception{
    		if(collection == null) return null;
    		Collection<T> newCollection = (Collection<T>) collection.getClass().newInstance();
    		 for(T obj:collection){
    			 newCollection.add((T) deepClone(obj));
    		 }
    		return newCollection;
    	}
    	
    	/**
    	 *<p>Title: cloneForClob</p>
    	 *<p>Description:对CLOB进行克隆</p>
    	 * @param @param clob
    	 * @param @return
    	 * @param @throws Exception 设定文件
    	 * @return  Clob 返回类型
    	 * @throws
    	*/
    	public Clob cloneForClob(Clob clob) throws Exception{
    		return clob;
    	}
    	
    	/**
    	 *<p>Title: cloneForClob</p>
    	 *<p>Description:对CLOB进行克隆</p>
    	 * @param @param clob
    	 * @param @return
    	 * @param @throws Exception 设定文件
    	 * @return  Clob 返回类型
    	 * @throws
    	*/
    	public Blob cloneForBlob(Blob blob) throws Exception{
    		return blob;
    	}
    	
    	/**
    	 *<p>Title: cloneForSpecial</p>
    	 *<p>Description:特殊类型克隆</p>
    	 *<p>List,Map,Set,Stack等</p>
    	 * @param @param <T>
    	 * @param @param obj
    	 * @param @return
    	 * @param @throws Exception 设定文件
    	 * @return  T 返回类型
    	 * @throws
    	*/
    	public <T>T cloneForSpecial(T obj) throws Exception{
    		if(ReflectUtil.isInterfaceToAchieve(obj.getClass(), "Collection"))return (T) cloneForCollection((Collection<?>) obj);
    		if(ReflectUtil.isInterfaceToAchieve(obj.getClass(), "Map"))return (T)cloneForMap((Map<?,?>) obj);
    		if(ReflectUtil.isInterfaceToAchieve(obj.getClass(), "Clob"))return (T)cloneForClob((Clob) obj);
    		if(ReflectUtil.isInterfaceToAchieve(obj.getClass(), "Blob"))return (T)cloneForBlob((Blob) obj);
    		return null;
    	}
    
    }
    
    /**
     * <p>description: 过滤器接口,过滤器,为真克隆,为假无动作</p>
     * <p>Date: Feb 8, 2012 2:44:08 PM</p>
     * <p>modify：</p>
     * @author: majin
     * @version: 1.0
     * </p>Company: 北京合力金桥软件技术有限责任公司</p>
     */
    public interface cloneFilter {
    	/** 过滤失败后无动作 */
    	public final static String NULL_FILTER_RETURN = "0";
    	/** 过滤失败后返回当前克隆对象引用 */
    	public final static String IMPORT_FILTER_RETURN = "1";
    	/**
    	 *<p>Title: filter</p>
    	 *<p>Description:过滤器,为真克隆,为假无动作</p>
    	 * @param @param <T>
    	 * @param @param obj
    	 * @param @return 设定文件
    	 * @return  boolean 返回类型
    	 * @throws
    	*/    	
    	public <T>boolean filter(T obj);
    }
    
}
    
    
