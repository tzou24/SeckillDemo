package org.cckj.serializable;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONSerializable {

	public static void main(String[] args) throws IOException {
		Person person = new Person();
		person.setAddress("hangzhou");
		person.setAge(24);
		person.setBirth(new Date());
		person.setName("zhangsan");
		
		//json 对象序列化
		String personJson = null;
		ObjectMapper mapper = new ObjectMapper();
		StringWriter sw = new StringWriter();
		JsonGenerator gen = new JsonFactory().createGenerator(sw);
		
		mapper.writeValue(gen, person);
		gen.close();
		personJson = sw.toString();
		
		//json 反序列化
		Person zhangsan = (Person) mapper.readValue(personJson, Person.class);
		
		System.out.println(personJson);
		System.out.println(zhangsan.getName());
	}
	
}
