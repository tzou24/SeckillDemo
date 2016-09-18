package org.cckj.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;

public class JavaSerializable {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		Person zhangsan = new Person();
		zhangsan.setAddress("hangzhou");
		zhangsan.setAge(24);
		zhangsan.setBirth(new Date());
		zhangsan.setName("zhangsan");
		
		//字节数组输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		//对象输出流
		ObjectOutputStream out = new ObjectOutputStream(os);
		//将对象写入到字节数组输出，进行序列化
		out.writeObject(zhangsan);
		
		byte[] zhangsanByte = os.toByteArray();
		
		//字节数组输入流
		ByteArrayInputStream is = new ByteArrayInputStream(zhangsanByte);
		//执行反序列化，从流中读取对象
		ObjectInputStream in = new ObjectInputStream(is);
		Person person = (Person)in.readObject();
	}
}
