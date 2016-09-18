package org.cckj.serializable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessianSerializable {

	public static void main(String[] args) throws IOException {
		
		Person zhangsan = new Person();
		zhangsan.setAddress("hangzhou");
		zhangsan.setAge(24);
		zhangsan.setBirth(new Date());
		zhangsan.setName("zhangsan");
		
		//字节数组输出流
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		
		HessianOutput ho = new HessianOutput(os);
		ho.writeObject(zhangsan);
		byte[] zhangsanByte = os.toByteArray();
		
		ByteArrayInputStream is = new ByteArrayInputStream(zhangsanByte);
		
		HessianInput hi = new HessianInput(is);
		Person person = (Person)hi.readObject();
		
		System.out.println("name: " + person.getName());
	}
}
