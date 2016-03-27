/**
 * bargetorCommon
 * com.bargetor.nest.common.util
 * TemplateUtil.java
 * 
 * 2015年6月15日-下午9:52:18
 *  2015Bargetor-版权所有
 *
 */
package com.bargetor.nest.common.util;

import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 *
 * TemplateUtil
 * 基于freemarker的模板工具
 * kin
 * kin
 * 2015年6月15日 下午9:52:18
 * 
 * @version 1.0.0
 *
 */
public class TemplateUtil {
	
	public static String build(Reader reader, Object data){
		try {
			Template template = new Template("", reader, null);
			Writer writer = new StringWriter();
			template.process(data, writer);
			return writer.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
