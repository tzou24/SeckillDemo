package org.cckj.serializable;

import java.io.Serializable;
import java.util.Date;

public class Person implements Serializable{

	/**
	 * 序列化ID  验证反序列化对象一致
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private int age;
	private String address;
	private Date birth;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Date getBirth() {
		return birth;
	}
	public void setBirth(Date birth) {
		this.birth = birth;
	}
	
}
