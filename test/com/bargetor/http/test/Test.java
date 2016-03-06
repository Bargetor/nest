package com.bargetor.http.test;

import java.util.HashMap;
import java.util.Map;

import com.bargetor.service.common.http.HttpRequester;
import com.bargetor.service.common.http.HttpResponse;
 
public class Test {
	public static void main(String[] args) {
		try {
			HttpRequester request = new HttpRequester();
			request.setDefaultContentEncoding("UTF-8");
			Map<String,String> pro = new HashMap<String, String>();
			pro.put("Content-Length","0");
			//HttpRespons hr = param.sendGet("http://www.kuaidi100.com/query?id=&postid=6381323593&valicode=&temp=0.5656136693471628&type=yuantong");
			//HttpRespons hr = param.sendGet("http://www.kuaidi100.com/query?type=zhongtong&postid=718249611213&id=1&valicode=&temp=0.8486089082487187");
			HttpResponse hr = request.sendPost("http://channel.api.duapp.com/rest/2.0/channel/channel/",pro,pro);
			
			System.out.println(hr.getUrlString());
			System.out.println(hr.getProtocol());
			System.out.println(hr.getHost());
			System.out.println(hr.getPort());
			System.out.println(hr.getContentEncoding());
			System.out.println(hr.getMethod());
			
			System.out.println(hr.getContent());
 
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
