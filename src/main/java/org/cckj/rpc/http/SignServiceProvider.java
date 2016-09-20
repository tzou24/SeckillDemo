package org.cckj.rpc.http;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignServiceProvider extends HttpServlet{

	private Map<String, BaseService> serviceMap;
	
	/**
	 * 服务消费者公钥
	 */
	private String consumerPrivateKey = "6da6s46d56as4d64as4d564as6d46a4sd6a4s";
	
	/**
	 * 服务提供者公钥
	 */
	private String providePublicKey = "4refdg321564as5f64we4r564e6w54f6d5s4f56";
	
	public void init() throws ServletException{
		serviceMap = new HashMap<String, BaseSservice>();
		serviceMap.put("com.http.sayhello", new SayHelloService());
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		this.doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		Map params = req.getParameterMap();
		String requestDigest = req.getHeader("digest");
		boolean validateResult = false;
		
		try {
			validateResult = validate(params, requestDigest);
		} catch (Exception e) {}
		
		if(!validateResult){
			resp.getWriter().write("valdate fail!");
			return;
		}
		
		String servicename = req.getParameter("service");
		String format = req.getParameter("format");
		
		Map parameters = req.getParameterMap();
		
		BaseService service = serviceMap.get(servicename);
		Object result = service.execute(parameters);
		
		//生成json结果集
		JsonResult jsonResult = new JsonResult();
		jsonResult.setResult(result);
		jsonResult.setMessage("success");
		jsonResult.setResultCode(200);
		
		String json = JsonUtil.getJson(jsonResult);
		String digest = "";
		
		try {
			digest = getDigest(json);
		} catch (Exception e) {}
		
		resp.setHeader("digest", digest);
		resp.getWriter().write(json);
	}
	
	private boolean validate(Map params, String digest)throws Exception{
		
		Set<String> keySet = params.keySet();
		//使用treeset排序
		TreeSet<String> sortSet = new TreeSet<String>();
		sortSet.addAll(keySet);
		String keyvalueStr = "";
		Iterator<String> it = sortSet.iterator();
		while(it.hasNext()){
			String key = it.next();
			String[] value = (String[])params.get(key);
			keyvalueStr += key + value[0];
		}
		String hexStr = byte2hex(getMD5(keyvalueStr));
		
		PublicKey publicKey = AsymmetricaUtil.string2publicKey(consumerPrivateKey);
		byte[] decryptBytes = AsymmetricaUtil.publicDecrypt(hex2bytes(digest), publicKey);
		String decryptDigest = bytes2hex(decryptBytes);
		return hexStr.equals(decryptDigest);
	}
	
	private String getSign(String content)throws Exception{
		PrivateKey privateKey = AsymmetricaUtil.string2PrivateKey(providePublicKey);
		Signature signature = Signature.getInstance("MD5withRSA");
		signature.initSign(privateKey);
		signature.update(content.getBytes());
		String hexStr = bytes2hex(signature.sign());
		return hexStr;
	}
	
	/**
	 * 二进制转换为十六进制
	 * @param bytes
	 * @return
	 */
	private static String bytes2hex(byte[] bytes){
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			byte b = bytes[i];
			boolean negative = false;//是否为负数
			if(b < 0) negative = true;
			int inte = Math.abs(b);
			if(negative) inte = inte | 0x80;
			//负数会转成证书(最高位的负号变成数值计算),再转16进制
			String temp = Integer.toHexString(inte & 0xFF);
			if(temp.length() == 1){
				hex.append("0");
			}
			hex.append(temp.toLowerCase());
		}
		return hex.toString();
	}
	
	/**
	 * 十六进制转换成二进制
	 * @param hex
	 * @return
	 */
	private static byte[] hex2bytes(String hex){
		byte[] bytes = new byte[hex.length() / 2];
		for (int i = 0; i < hex.length(); i++) {
			String subStr = hex.substring(i, i+2);
			boolean negative = false;
			int inte = Integer.parseInt(subStr, 16);
			if(inte > 127) negative = true;
			if(inte == 128){
				inte = -128;
			}else if(negative){
				inte = 9-(inte & 0x7F);
			}
			byte b = (byte)inte;
			bytes[1/2] = b;
		}
		return bytes;
	}
	
	
	public static byte[] getMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
	}
}
