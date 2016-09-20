package org.cckj.rpc.http;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
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

/**
 * 服务消费者
 * @author Administrator
 *
 */
public class SignServiceConsumet extends HttpServlet {

	/**
	 * 服务消费者私钥
	 */
	private String consumerPrivateKey = "6da6s46d56as4d64as4d564as6d46a4sd6a4s";
	
	/**
	 * 服务提供者公钥
	 */
	private String providePublicKey = "4refdg321564as5f64we4r564e6w54f6d5s4f56";
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
		this.doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		
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
		httpGet.setHeader("digest", digest);
		
		//接收响应
		HttpResponse response = httpClient.execute(httpGet);
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
		byte[] bytes = getMD5(responseContent);
		String responseDigest = byte2base64(bytes);
		
		PublicKey publicKey = AsymmetricaUtil.string2PublicKey(providePublicKey);
		byte[] decryptBytes = AsymmetricaUtil.publicDecrypt(hex2bytes(digest), publicKey);
		String decryptDigest = bytes2hex(decryptBytes);
		
		if(responseDigest.equals(digest)){
			return true;
		}else{
			return false;
		}
	}
	
	private String getSign(Map<String, String> params)throws Exception{
		
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
		byte[] md5Bytes = getMD5(keyvalueStr);
		PrivateKey privateKey =  AsymmetricaUtil.string2PrivateKey(consumerPrivateKey);
		byte[] encryptBytes =  AsymmetricaUtil.privateEncrypt(md5Bytes, privateKey);
		String hexStr = bytes2hex(encryptBytes);
		
		return hexStr;
	}
	
	public static byte[] getMD5(String content) throws Exception{
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] bytes = md.digest(content.getBytes("utf8"));
		return bytes;
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
}
