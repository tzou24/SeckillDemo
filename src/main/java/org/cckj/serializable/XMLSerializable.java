package org.cckj.serializable;

import java.util.Date;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XMLSerializable {

	
	public static void main(String[] args) {
		
		Person person = new Person();
		person.setAddress("hangzhou");
		person.setAge(24);
		person.setBirth(new Date());
		person.setName("zhangsan");
		
		//将person对象序列化为xml
		XStream xStream = new XStream(new DomDriver());
		
		//设置person类的别名
		xStream.alias("person", Person.class);
		String personXML = xStream.toXML(person);
		
		//将xml反序列化还原为person对象
		Person zhangsan = (Person)xStream.fromXML(personXML);
		
		System.out.println(personXML);
		System.out.println(zhangsan.getName());
		
	}
}
