//package org.cckj.rpc.http;
//
//import java.io.IOException;
//import java.util.Map;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.http.HttpEntity;
//
//import sun.net.www.http.HttpClient;
//
///**
// * 服务提供者
// * @author Administrator
// *
// */
//public class ServiceProvider extends HttpServlet {
//
//	private Map<String, BaseService> serviceMap; 
//	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
//		this.doPost(req, resp);
//	}
//	
//	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
//		
//		String servicename = req.getParameter("service");
//		String format = req.getParameter("format");
//		
//		Map parameters = req.getParameterMap();
//		
//		BaseService service = serviceMap.get(servicename);
//		
//		Object result = service.execute(parameters);
//		
//		//生成json结果集
//		JsonResult jsonResult = new JsonResult();
//		jsonResult.setResult(result);
//		jsonResult.setMessage("success");
//		jsonResult.setResultCode(200);
//		
//		String json = JsonUtil.getJson(jsonResult);
//		resp.getWriter().write(json);
//		
//	}
//}
