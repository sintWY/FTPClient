package com.truncate;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * 描述: Channel工厂类
 * 版权: Copyright (c) 2016
 * 公司: 思迪科技 
 * 作者: 王功俊(wanggj@thinkive.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月09日
 * 创建时间: 16:22
 */
public class ChannelFactory
{

	private static final Logger logger = Logger.getLogger(ChannelFactory.class);

	/**
	 *@描述：获取指定类型的channel
	 *@作者:王功俊(wanggj@thinkive.com)
	 *@日期:2016/12/9
	 *@时间:16:31
	 */
	public static Channel getChannel(AuthConfig config, String type)
	{

		try
		{
			JSch jSch = new JSch();
			Session session = jSch.getSession(config.getUserName(), config.getIp(), Integer.valueOf(config.getPort()));
			logger.debug("session is created...：" + session);
			session.setPassword(config.getPassword());
			Properties properties = new Properties();
			properties.put("StrictHostKeyChecking", "no");
			session.setConfig(properties); // 为Session对象设置properties
			session.setTimeout(config.getTimeout()); // 设置timeout时间
			session.connect(); // 通过Session建立链接
			logger.debug("session connected...：" + session);
			Channel channel = session.openChannel(type);
			logger.info("Connected successfully to ip：" + config.getIp() + ",username：" + config.getUserName() + ",session：" + session + ",channel：" + session);
			return channel;
		}
		catch(JSchException e)
		{
			logger.error("", e);
		}
		return null;
	}
}
