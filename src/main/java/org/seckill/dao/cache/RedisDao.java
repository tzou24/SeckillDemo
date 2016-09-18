package org.seckill.dao.cache;

import org.seckill.entity.Seckill;
import org.slf4j.LoggerFactory;

import org.slf4j.Logger;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private JedisPool jedisPool;

	//初始化
	public RedisDao(String ip, int port) {
		jedisPool = new JedisPool(ip, port);
	}
	
	//获得该序列化的 对象类 的结构
	private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);
	
	public Seckill getSeckill(long seckillId) {
		//缓存redis 操作逻辑
		try {
			Jedis jedis = jedisPool.getResource();//连接池
			try {
				String key = "seckill:" + seckillId;
				//并没有实现内部序列化操作
				//get -> byte[] -> 反序列化 -> Object(Seckill)
				//采用自定义序列化
				//protostuff : pojo.
				byte[] bytes = jedis.get(key.getBytes());
				//缓存获取到
				if(bytes != null){
					Seckill seckill = schema.newMessage();
					ProtostuffIOUtil.mergeFrom(bytes, seckill, schema);
					//seckill 被反序列化
					//比 java 原生要 优化  1/4
					return seckill;
				}
			} finally {
				jedis.close();
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}

	public String putSeckill(Seckill seckill) {
		//set Object(Seckill) -> 序列化 -> bute[]
		try {
			Jedis jedis = jedisPool.getResource();
			try {
				String key = "seckill:" + seckill.getSeckillId();   
				byte[] bytes = ProtostuffIOUtil.toByteArray(seckill, schema, 
						//缓存器   默认缓存器大小， 特别大会有缓存
						LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
				int timeout = 60*60;
				// ok  错误信息
				String result = jedis.setex(key.getBytes(), timeout, bytes);
				return result;
			} finally {
				jedis.close();
			}
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return null;
	}
}
