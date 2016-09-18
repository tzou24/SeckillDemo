package org.cckj.validatefile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/** 
 * 判断文件类型
 * @author Administrator
 *
 */
public class FileTypeJudge {

	/**
	 * 二进制转化为 16进制
	 * @param bytes
	 * @return
	 */
	private static String bytes2hex(byte[] bytes){
		StringBuilder hex = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			String temp = Integer.toHexString(bytes[i] & 0xFF);
			if(temp.length() == 1){
				hex.append("0");
			}
			hex.append(temp.toLowerCase());
		}
		return hex.toString();
	}
	
	/**
	 * 读取文件头
	 * @return
	 * @throws IOException
	 */
	private static String getFileHeader(String filePath)throws IOException{
		//这里需要注意：每个文件的魔数(magic word)的长度都不相同，因此需要使用startwith
		byte[] b = new byte[28];
		InputStream inputStream = null;
		inputStream = new FileInputStream(filePath);
		inputStream.read(b, 0, 28);
		inputStream.close();
		
		return bytes2hex(b);
	}
	
	public static FileType getType(String filePath)throws IOException{
		String fileHead = getFileHeader(filePath);
		if(fileHead == null || fileHead.length() == 0){
			return null;
		}
		fileHead = fileHead.toUpperCase();
		FileType[] fileTypes = FileType.values();
		for (FileType fileType : fileTypes) {
			if(fileHead.startsWith(fileType.getValue())){
				return fileType;
			}
		}
		return null;
	}
	
	public static void main(String[] args) throws IOException {
		
		System.out.println(FileTypeJudge.getType("D:\\Download\\doc.rar"));
	}
}
