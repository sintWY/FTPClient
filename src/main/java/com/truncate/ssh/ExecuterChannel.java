package com.truncate.ssh;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.truncate.AuthConfig;
import com.truncate.ChannelFactory;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;

/**
 * 描述: //TODO 类描述
 * 版权: Copyright (c) 2016
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月09日
 * 创建时间: 16:13
 */
public class ExecuterChannel
{

	private static final Logger logger = Logger.getLogger(ExecuterChannel.class);

	//块大小
	private static final int BLOCK_SIZE = 1024;

	private AuthConfig config;

	public ExecuterChannel(AuthConfig config)
	{
		this.config = config;
	}

	public String execute(String command) throws IOException, JSchException
	{
		ChannelExec channelExec = (ChannelExec) ChannelFactory.getChannel(config, "exec");
		InputStream resultStream = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.connect();
		closeChannel(channelExec);
		return generateResult(resultStream);
	}

	private String generateResult(InputStream inputStream) throws IOException
	{
		StringBuilder buffer = new StringBuilder();
		byte[] bytes = new byte[BLOCK_SIZE];
		while(inputStream.read(bytes) > 0)
		{
			buffer.append(new String(bytes));
		}
		return buffer.toString();
	}

	public void closeChannel(ChannelExec channelExec)
	{
		try
		{
			if(channelExec != null)
			{
				channelExec.disconnect();
				channelExec.getSession().disconnect();
			}
		}
		catch(JSchException e)
		{
			logger.error("", e);
		}
	}
}
