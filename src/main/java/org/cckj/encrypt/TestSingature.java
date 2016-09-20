package org.cckj.encrypt;

import java.io.IOException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 数字签名API使用
 * @author Administrator
 *
 */
public class TestSingature {

	public static KeyPair getKeyPair() throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(512);
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}
	
	/**
	 * 
	 * @param keyPair
	 * @return
	 */
	public static String getPublicKey(KeyPair keyPair){
		PublicKey publicKey = keyPair.getPublic();
		byte[] bytes = publicKey.getEncoded();
		return byte2base64(bytes);
	}
	
	public static String getPrivateKey(KeyPair keyPair){
		PrivateKey privateKey = keyPair.getPrivate();
		byte[] bytes = privateKey.getEncoded();
		return byte2base64(bytes);
	}
	
	public static PublicKey string2PublicKey(String pubStr) throws Exception{
		byte[] keyBytes = base642byte(pubStr);
		
		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}
	
	public static PrivateKey string2PrivateKey(String priStr)throws Exception{
		byte[] keyBytes = base642byte(priStr);
		
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}
	
	/**
	 * 对字节 base 64位编码
	 * @param bytes
	 * @return
	 */
	private static String byte2base64(byte[] bytes){
		BASE64Encoder base64Encoder = new BASE64Encoder();
		return base64Encoder.encode(bytes);
	}
	
	private static byte[] base642byte(String base64) throws IOException{
		BASE64Decoder base64Decoder = new BASE64Decoder();
		return base64Decoder.decodeBuffer(base64);
	}
	
	/**
	 * 数字签名生成
	 * @param content
	 * @param privateKey
	 * @return
	 * @throws Exception
	 */
	private static byte[] sign(byte[] content, PrivateKey privateKey) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] bytes = md.digest(content);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, privateKey);
		byte[] encryptBytes = cipher.doFinal(bytes);
		return encryptBytes;
	}
	
	/**
	 * 数字签名的校验
	 * @param content
	 * @param sign
	 * @param publicKey
	 * @return
	 * @throws Exception
	 */
	private static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) throws Exception{
		MessageDigest md = MessageDigest.getInstance("SHA1");
		byte[] bytes = md.digest(content);
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, publicKey);
		byte[] decryptBytes = cipher.doFinal(sign);
		return byte2base64(decryptBytes).equals(byte2base64(bytes));
	}
	
	/**
	 * test
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		KeyPair keyPair = getKeyPair(); //生成一对密钥
		String publicKey = getPublicKey(keyPair); //得到公钥字符串
		String privateKey = getPrivateKey(keyPair);//得到私钥字符串
		System.out.println(publicKey);
		System.out.println(privateKey);
		PublicKey publics = string2PublicKey(publicKey);//转换成公钥对象
		PrivateKey privates = string2PrivateKey(privateKey);//转换成私钥对象
		
		String a = "hello, i am checkangxiao,good night";
		
		byte[] sign = sign(a.getBytes(), privates); //进行签名
		String b = "hello,i am checkangxiao,good night";
		boolean verify = verify(b.getBytes(), sign, publics);// 用公钥来进行校验
		
		System.out.println(verify); //得到结果
		/*
		 * MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIfeAFOGHNgo6B60bs6Cp7/TYW/6bxW9rw87HdZM2tv1
		 * tHCmXjdzh4fMqQrWJczCevDidzeZdd7ldN5M43nchT8CAwEAAQ==
		 * 
		 * MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOKM0XOvzQo49VR450FB6l4dvgJyb0gAm/BFU214kCOt
		 * p01UcWugDltCqWUJGA8keedWiC372IzeQst/ik9v9E0CAwEAAQ==
		 */
	}
}
