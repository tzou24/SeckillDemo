//package org.cckj.rpc.http;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import sun.net.www.http.HttpClient;
//
///**
// * 服务消费者
// * @author Administrator
// *
// */
//public class ServiceConsumer extends HttpServlet {
//
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
//		this.doPost(req, resp);
//	}
//	
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
//		
//		//参数
//		String service = "com.http.sayhello";
//		String format = "json";
//		String arg1 = "hello";
//		
//		String url = "http://localhost:8080/testhttprpc/provider.do?" + "service=" + service + "&format=" + format + "&arg1=" + arg1;
//		
//		//组装请求
//		HttpClient httpClient = new DefaultHttpClient();
//		HttpGet httpGet = new HttpGet(url);
//		
//		HttpResponse response = httpClient.execute(httpGet);
//		
//		HttpEntity entity = response.getEntity();
//		byte[] bytes = EntityUtils.toByteArray(entity);
//		String jsonresult = new String(bytes, "utf8");
//		
//		JsonResult result = (JsonResult)JsonUtil.jsonToObject(jsonresult, JsonResult.class);
//		
//		resp.getWriter().write(result.getResult().toString());
//	}
//}
