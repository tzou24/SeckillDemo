package org.cckj.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProtocolUtil {

	public static Request readRequest(InputStream input) throws IOException {

		// 读取字节
		byte[] encodeByte = new byte[1];
		input.read(encodeByte);
		byte encode = encodeByte[0];

		// 读取命令长度
		byte[] commandLengthBytes = new byte[4];
		input.read(commandLengthBytes);
		int commandLength = ByteUtil.bytes2Int(commandLengthBytes);

		// 读取命令
		byte[] commandBytes = new byte[commandLength];
		input.read(commandBytes);
		String command = "";
		if (Encode.GBK.getValue() == encode) {
			command = new String(commandBytes, "GBK");
		} else {
			command = new String(commandBytes, "UTF8");
		}

		// 组装请求返回
		Request request = new Request();
		request.setCommand(command);
		request.setEncode(encode);
		request.setCommandLength(commandLength);

		return request;
	}

	public static Response readResponse(InputStream input) throws IOException {

		// 读取编码
		byte[] encodeByte = new byte[1];
		input.read(encodeByte);
		byte encode = encodeByte[0];

		// 读取响应长度
		byte[] responseLengthByte = new byte[4];
		input.read(responseLengthByte);
		int responseLength = ByteUtil.bytes2Int(responseLengthByte);

		// 读取响应内容
		byte[] responseBytes = new byte[responseLength];
		input.read(responseBytes);
		String responseStr = "";

		// 设置编码
		if (Encode.GBK.getValue() == encode) {
			responseStr = new String(responseBytes, "GBK");
		} else {
			responseStr = new String(responseBytes, "UTF8");
		}

		// 组装响应
		Response response = new Response();
		response.setEncode(encode);
		response.setResponse(responseStr);
		response.setResponseLength(responseLength);

		return response;
	}

	public static void writeResponse(OutputStream output, Response response)
			throws IOException {
		// 将response响应返回给客户端
		output.write(response.getEncode());
		// output.write(response.getResponseLength()); 会截取低8位传输，丢弃高24位
		output.write(ByteUtil.int2ByteArray(response.getResponseLength()));
		if (Encode.GBK.getValue() == response.getEncode()) {
			output.write(response.getResponse().getBytes("GBK"));
		} else {
			output.write(response.getResponse().getBytes("UTF8"));
		}
		output.flush();
	}

	public static void writeRequest(OutputStream output, Request request)
			throws IOException {

		// 将reqeust请求发送给服务端
		output.write(request.getEncode());
		output.write(ByteUtil.int2ByteArray(request.getCommandLength()));
		if (Encode.GBK.getValue() == request.getEncode()) {
			output.write(request.getCommand().getBytes("GBK"));
		} else {
			output.write(request.getCommand().getBytes("UTF8"));
		}

		output.flush();
	}
}
