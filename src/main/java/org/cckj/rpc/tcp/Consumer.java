package org.cckj.rpc.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户 消费者
 * @author Administrator
 *
 */
public class Consumer {

	public static void main(String[] args) throws NoSuchMethodException, SecurityException, UnknownHostException, IOException, ClassNotFoundException {
		
		//接口名称
		String interfacename = SayHelloService.class.getName();
		//需要远程执行的方法
		Method method = SayHelloService.class.getMethod("sayHello", java.lang.String.class);
		//需要传递到远端的参数
		Object[] arguments = {"hello"};
		
		Socket socket = new Socket("127.0.0.1", 1234);
//		将方法名称和参数传递到远端
		ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
		output.writeUTF(interfacename);
		output.writeUTF(method.getName());
		output.writeObject(method.getParameterTypes());
		output.writeObject(arguments);
		
		//从远端读取方法执行结果
		ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
		Object result = input.readObject();
		
//		使用代理对象来处理，直接返回string类型
		System.out.println(result);
	}
}
