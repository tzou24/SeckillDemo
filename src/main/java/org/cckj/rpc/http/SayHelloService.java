//package org.cckj.rpc.http;
//
//import java.util.Map;
//
//public class SayHelloService implements BaseService{
//
//	public Object execute(Map<String, Object> args){
//		//request.getParameterMap() 取出来为Array 此处需要注意
//		String[] helloAry = (String[]) args.get("arg1");
//		if("hello".equals(helloAry[0])){
//			return "hello";
//		}else{
//			return "bye bye";
//		}
//	}
//}
