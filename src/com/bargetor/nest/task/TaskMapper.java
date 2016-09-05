/**
 * Migrant
 * com.bargetor.migrant.model.task
 * TaskMapper.java
 * 
 * 2015年6月22日-下午7:45:08
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.task;

import com.bargetor.nest.task.bean.Task;
import org.apache.ibatis.annotations.*;

import java.math.BigInteger;
import java.util.List;

/**
 *
 * TaskMapper
 * 
 * kin
 * kin
 * 2015年6月22日 下午7:45:08
 * 
 * @version 1.0.0
 *
 */
public interface TaskMapper {
	
	@Select("select * from TBL_TASK where taskId = #{taskId}")
	public Task getTask(BigInteger taskId);
	
	@Select("select * from TBL_TASK where type = #{type}")
	public List<Task> getTaskByType(String type);
	
	@Select("select * from TBL_TASK where type = #{type} and status = #{status} and tag = #{tag}")
	public List<Task> getTaskByTypeStatusTag(@Param("type") String type, @Param("status") String status, @Param("tag") String tag);
	
	@Select("select * from TBL_TASK where type = #{type} and status = #{status} and tag = #{tag} limit 0,1")
	public Task getOneTaskByTypeStatusTag(@Param("type") String type, @Param("status") String status, @Param("tag") String tag);
	
	@Select("select * from TBL_TASK where type = #{type} and status = #{status}")
	public List<Task> getTaskByTypeStatus(@Param("type") String type, @Param("status") String status);
	
	@Update("update TBL_TASK set status = #{status} where taskId = #{taskId}")
	public int updateTaskStatus(@Param("taskId") BigInteger taskId, @Param("status") String status);

	@Update("update TBL_TASK set attributeJson = #{attributeJson} where taskId = #{taskId}")
	public int updateTaskAttributeJson(@Param("taskId") BigInteger taskId, @Param("attributeJson") String attributeJson);

	@Update("update TBL_TASK set errorJson = #{errorJson} where taskId = #{taskId}")
	public int updateTaskErrorJson(@Param("taskId") BigInteger taskId, @Param("errorJson") String errorJson);

	@Select("select attributeJson from TBL_TASK where taskId = #{taskId}")
	public String getTaskAttributeJson(@Param("taskId") BigInteger taskId);

	@Select("select errorJson from TBL_TASK where taskId = #{taskId}")
	public String getTaskErrorJson(@Param("taskId") BigInteger taskId);

	@Insert("insert into TBL_TASK(taskId, type, tag, status, attributeJson, errorJson, createTime) values (#{taskId}, #{type}, #{tag}, #{status}, #{attributeJson}, #{errorJson}, #{createTime})")
	@Options(useGeneratedKeys = true, keyProperty = "taskId", keyColumn = "taskId")
	public int createTask(Task task);
	
	@Select("select * from TBL_TASK where type = #{type} and status = #{status} limit 0,1")
	public Task getOneTaskByTypeStatus(@Param("type") String type, @Param("status") String status);
	
	@Delete("delete from TBL_TASK where taskId = #{taskId}")
	public int deleteTask(BigInteger taskId);
}
