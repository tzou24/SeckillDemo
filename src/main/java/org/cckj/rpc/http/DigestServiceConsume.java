package org.cckj.rpc.http;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.netty.handler.codec.http.HttpResponse;

import sun.misc.BASE64Encoder;
import sun.net.www.http.HttpClient;

/**
 * 参数摘要加密
 * @author Administrator
 *
 */
public class DigestServiceConsume extends HttpServlet{

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		this.doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
		
		//参数
		String service = "com.http.sayhello";
		String format = "json";
		String arg1 = "hello";
		
		Map<String, String> params = new HashMap<String, String>();
		params.put("service", service);
		params.put("format", format);
		params.put("arg1", arg1);
		String digest = "";//摘要
		try{
			digest = this.getDigest(params);
		}catch(Exception e){}
		
		String url = "http://localhost:8080/testhttprpc/provider.do?" + "service=" + service + "&format=" + format + "&arg1=" + arg1;
		
		//组装请求
//		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
//		
		HttpResponse response = httpClient.execute(httpGet);
//		
//		HttpEntity entity = response.getEntity();
//		byte[] bytes = EntityUtils.toByteArray(entity);
//		String jsonresult = new String(bytes);
		
		//从header里面接收摘要
		String serverResponseDigest = response.getLastHeader("digest").getValue();
		boolean validateResult = false;
		
		try {
			validateResult = validate(jsonresult, serverResponseDigest);
		} catch (Exception e) {}
		
		if(validateResult){
			JsonResult result = (JsonResult)JsonUtil.jsonToObject(jsonresult, JsonResult.class);
			resp.getWriter().write(result.getResult().toString());
		}else{
			resp.getWriter().write("validate fail!");
		}
		
	}
	
	/**
	 * 响应验证
	 * @param responseContent
	 * @param digest
	 * @return
	 * @throws Exception
	 */
	private boolean validate(String responseContent, String digest)throws Exception{
		String secret = "abcdefghijklmn";
		byte[] bytes = getMD5(responseContent + secret);
		String responseDigest = byte2base64(bytes);
		if(responseDigest.equals(digest)){
			return true;
		}else{
			return false;
		}
	}
	
	private String getDigest(Map<String, String> params)throws Exception{
		String secret = "abcdefghijklmn";
		
		Set<String> keySet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(keySet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = params.get(key);
			keyvalueStr += key + value;
		}
		keyvalueStr += secret;
		String base64Str = byte2base64(getMD5(keyvalueStr));
		return base64Str;
	}
	
	private static String byte2base64(byte[] bytes){
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}
	
	public static byte[] getMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
	}
}
