package org.cckj.socket;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException,
			IOException {
		Request request = new Request();
		request.setCommand("HELLO");
		request.setCommandLength(request.getCommand().length());
		request.setEncode(Encode.UTF8.getValue());

		// 连接服务端
		Socket client = new Socket("127.0.0.1", 4567);

		OutputStream output = client.getOutputStream();

		ProtocolUtil.writeRequest(output, request);

		// 读取响应数据
		InputStream input = client.getInputStream();
		Response response = ProtocolUtil.readResponse(input);
		System.out.println(response.getResponse());
		client.shutdownOutput();

		//

	}
}
