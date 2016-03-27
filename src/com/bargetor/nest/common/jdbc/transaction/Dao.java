package com.bargetor.nest.common.jdbc.transaction;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.bargetor.nest.common.util.ReflectUtil;
import com.bargetor.nest.common.util.StringUtil;

/**
 * <p>description: 基础DAO</p>
 * <p>Date: 2013-9-23 下午06:06:58</p>
 * <p>modify：</p>
 * @author: Madgin
 * @version: 1.0
 */
public class Dao{
	private final static Logger logger = Logger.getLogger(Dao.class);
	
	private Transaction transaction;
	
	public Dao(String name) {
		this.transaction = new Transaction(name);
		this.transaction.startTransaction();
	}
	
	/**
	 *<p>Title: getConnection</p>
	 *<p>Description:获取连接</p>
	 * @return
	 * @return Connection 返回类型
	*/
	public Connection getConnection() {
		return this.transaction.getConnection();
	}

	/**
	 *<p>Title: executeSql</p>
	 *<p>Description:执行SQL</p>
	 * @param sql
	 * @param params
	 * @return void 返回类型
	*/
	public void executeSql(String sql, Object[] params) {
		this.querySql(sql, params);
	}
	

	/**
	 *<p>Title: executeSql</p>
	 *<p>Description:执行SQL</p>
	 * @param sql
	 * @return void 返回类型
	*/
	public void executeSql(String sql) {
		Statement sta = this.transaction.getStatement();
		try {
			logger.info(sql); 
			sta.execute(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			this.transaction.rollback();
		}
	}
	
	/**
	 *<p>Title: querySql</p>
	 *<p>Description:查询SQL</p>
	 * @param sql
	 * @return
	 * @return ResultSet 返回类型
	*/
	public ResultSet querySql(String sql) {
		try {
			Statement sta = this.transaction.getStatement();
			logger.info(sql);
			return sta.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			this.transaction.rollback();
			return null;
		}
	}
	
	/**
	 *<p>Title: querySql</p>
	 *<p>Description:查询SQL</p>
	 * @param sql
	 * @param params
	 * @return
	 * @return ResultSet 返回类型
	*/
	public ResultSet querySql(String sql, Object[] params) {
		try {
			if(StringUtil.isNullStr(sql))return null;
			if(params == null)return null;
			PreparedStatement sta = this.transaction.getPreparedStatement(sql);
			if(params != null){
				for(int i = 0,len = params.length;i<len;i++){
					sta.setObject(i + 1, params[i]);
				}				
			}
			logger.info(sql);
			return sta.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
			this.transaction.rollback();
			return null;
		}
	}
	
	/**
	 *<p>Title: queryObject</p>
	 *<p>Description:查询对象</p>
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @return
	 * @return T 返回类型
	*/
	public <T> T queryObject(String sql,Class<T> clazz){
		logger.info(sql);
		return queryObject(sql,null, clazz);
	}
	
	/**
	 *<p>Title: queryObject</p>
	 *<p>Description:查询对象</p>
	 * @param <T>
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 * @return T 返回类型
	*/
	public <T> T queryObject(String sql, Object[] params, Class<T> clazz) {
		List<T> resultList = queryObjectList(sql, clazz, params);
		if(resultList == null)return null;
		if(resultList.size() <= 0)return null;
		return resultList.get(0);
	}
	
	/**
	 *<p>Title: queryObjectList</p>
	 *<p>Description:查询对象列表</p>
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @return
	 * @return List<T> 返回类型
	*/
	public <T> List<T> queryObjectList(String sql, Class<T> clazz) {
		return queryObjectList(sql, clazz, null);
	}
	
	/**
	 *<p>Title: queryObjectList</p>
	 *<p>Description:查询对象列表</p>
	 * @param <T>
	 * @param sql
	 * @param clazz
	 * @param params
	 * @return
	 * @return List<T> 返回类型
	*/
	public <T> List<T> queryObjectList(String sql, Class<T> clazz, Object[] params) {
		ResultSet rs = querySql(sql, params);
		return rsExtractorToObjectList(rs,clazz);
	}
	
	/**
	 *<p>Title: commit</p>
	 *<p>Description:提交</p>
	 * @return void 返回类型
	*/
	public void commit(){
		this.transaction.commit();
	}
	
	
	
	/*************************************** private methods ******************************************/
	
	/**
	 *<p>Title: rsExtractorToObjectList</p>
	 *<p>Description:结果集转换成对象列表</p>
	 * @param @param <T>
	 * @param @param rs
	 * @param @param clazz
	 * @param @return 
	 * @return  List<T> 
	 * @throws
	*/
	private <T> List<T> rsExtractorToObjectList(ResultSet rs,Class<T> clazz){
		List<T> result = new ArrayList<T>();
		try {
			if(rs == null)return result;
			//获取结果集列信息
			List<String> columns = getRSColumns(rs);
			Map<String,Method> columnSetMethodMap = filtrateSetMethodMap(clazz, columns);
			while(rs.next()){
				//新建对象
				T newObject = ReflectUtil.newInstance(clazz);
				//避免结果集列表中的列不存在于类中，故此处以Map中的函数信息为主
				for(String column : columns){
					//赋值
					invoke(newObject, columnSetMethodMap.get(column), rs, column);
				}
				result.add(newObject);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	} 
	
	/**
	 *<p>Title: getRSColumns</p>
	 *<p>Description:获取结果集列信息</p>
	 * @param @param rs
	 * @param @return 
	 * @return  List<String> 
	 * @throws
	*/
	private List<String> getRSColumns(ResultSet rs){
		List<String> columns = new ArrayList<String>();
		if(rs == null)return columns;
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int i = 1,len = rsmd.getColumnCount();i <= len;i++){
				columns.add(rsmd.getColumnLabel(i).toLowerCase());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return columns;
	}
	
	/**
	 *<p>Title: filtrateSetMethodMap</p>
	 *<p>Description:获取类的赋值函数键值对</p>
	 * @param @param clazz
	 * @param @param columnNames
	 * @param @return 
	 * @return  Map<String,Method> 
	 * @throws
	*/
	private Map<String, Method> filtrateSetMethodMap(Class<?> clazz, List<String> columnNames) {
		Map<String, Method> setMap = new HashMap<String, Method>(columnNames.size());
		Method[] methods = clazz.getMethods();
		for (String column : columnNames) {
			for(Method method : methods){
				if(method.getName().toLowerCase().equals("set" + column)){
					setMap.put(column, method);					
				}
			}
		}
		return setMap;
	}
	
	/**
	 *<p>Title: invoke</p>
	 *<p>Description:赋值</p>
	 * @param @param object
	 * @param @param setMethod
	 * @param @param rs
	 * @param @param name 
	 * @return  void 
	 * @throws
	*/
	private void invoke(Object object, Method setMethod, ResultSet rs, String name) {
		try {
			if(object == null || setMethod == null || StringUtil.isNullStr(name) || rs == null)return;
			Object parameter = null;
			Class<?> type = setMethod.getParameterTypes()[0];
			if (type.equals(String.class)) {
				parameter = rs.getString(name);
			} else if (type.equals(int.class) || type.equals(Integer.class)) {
				parameter = rs.getInt(name);
			} else if (type.equals(long.class) || type.equals(Long.class)) {
				parameter = rs.getLong(name);
			} else if (type.equals(byte.class) || type.equals(Byte.class)) {
				parameter = rs.getByte(name);
			} else if (type.equals(short.class) || type.equals(Short.class)) {
				parameter = rs.getShort(name);
			} else if (type.equals(float.class) || type.equals(Float.class)) {
				parameter = rs.getFloat(name);
			} else if (type.equals(double.class) || type.equals(Double.class)) {
				parameter = rs.getDouble(name);
			} else if (type.equals(Date.class) || type.equals(Timestamp.class)) {
				parameter = rs.getTimestamp(name);
			} else {
				/* doNothing */
			}
			if (parameter != null) {
				setMethod.invoke(object, parameter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


}
