package org.cckj.rpc.tcp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务提供者
 * @author Administrator
 *
 */
public class Provider {

	//所有服务
	private static Map<String, Object> services = new HashMap<String, Object>();
	
	static{
		services.put(SayHelloService.class.getName(), new SayHelloServiceImpl());
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		 
		ServerSocket server = new ServerSocket(1234);
		
		while (true) {
			Socket socket = server.accept();
//			读取服务信息
			ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
			String interfacename = input.readUTF();
			String methodName = input.readUTF();
			Class<?>[] parameterTypes = (Class<?>[])input.readObject();//参数类型
			Object[] arguments = (Object[])input.readObject();//参数对象
			
			Class serviceinterfaceclass = Class.forName(interfacename);///得到接口的class
			Object service = services.get(interfacename);//取得服务实现的对象
			Method method = serviceinterfaceclass.getMethod(methodName, parameterTypes);//获得要调用的方法
			Object result = method.invoke(service, arguments);
			
			ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
			output.writeObject(result);
		}
	}
}
