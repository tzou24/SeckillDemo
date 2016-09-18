package org.cckj.routeandloadbalance;

import java.net.InetAddress;

import org.I0Itec.zkclient.ZkClient;

/**
 * service provider A 实现
 * @author Administrator
 *
 */
public class ServiceAProvider {

	private String serviceName = "service-A";
	
	//向zookeeper 注册服务
	public void init() throws Exception{
		
		String serverList = "192.168.136.130:2181";
		String PATH = "/configcenter"; //根节点路径
		ZkClient zkClient = new ZkClient(serverList);
		
		boolean rootExists = zkClient.exists(PATH);
		
		if(!rootExists){
			zkClient.createPersistent(PATH);
		}
		
		boolean serviceExists = zkClient.exists(PATH + "/" + serviceName);
		if(!serviceExists){
			zkClient.createPersistent(PATH + "/" + serviceName);//创建服务节点
		}
		//注册当前服务器和权重
		InetAddress addr = InetAddress.getLocalHost();
		String ip = addr.getHostAddress().toString();//获取本机IP
		
		zkClient.createEphemeral(PATH + "/" + serviceName + "/" + ip);
	}
	
	//提供服务
	public void provide(){
		
	}
	
	public static void main(String[] args) throws Exception {
		ServiceAProvider a = new ServiceAProvider();
		a.init();
		
		Thread.sleep(1000 * 60 * 60 * 24);
	}
}
