package org.cckj.routeandloadbalance;

import java.util.ArrayList;
import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;


public class ServiceConsumer {

	private List<String> serverList = new ArrayList<String>();
	
	private String serviceName = "service-B";
	
	//初始化服务地址信息
	public void init(){
		
		String serviceName = "service-B";
		String zkServiceList = "192.168.136.130.2181"; //zookeeper 服务列表
		String SERVICE_PATH = "/configcenter/" + serviceName;//服务器节点路径
		ZkClient zkClient = new ZkClient(zkServiceList);
		
		boolean serviceExists = zkClient.exists(SERVICE_PATH);
		if(serviceExists){ //服务存在，取地址列表
			serverList = zkClient.getChildren(SERVICE_PATH);
		}else{
			throw new RuntimeException("service not exist");
		}
		
		//注册时间监听
		zkClient.subscribeChildChanges(SERVICE_PATH, new IZkChildListener() {
			
			//服务器动态变化，上线，下线，宕机时。就会触发该监听器，更新该服务列表
			@Override
			public void handleChildChange(String arg0, List<String> currentChilds)
					throws Exception {
				serverList = currentChilds; //得到服务列表
			}
		});
	}
	
	//消费服务
	public void consume(){
		//通过负载均衡算法，找到一台服务器进行调用
	}
	
	public static void main(String[] args) throws Exception {
		ServiceConsumer consumer = new ServiceConsumer();
		consumer.init();
		
		Thread.sleep(1000 * 60 * 60 * 24);
	}
}
