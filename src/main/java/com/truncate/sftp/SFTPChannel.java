package com.truncate.sftp;

import com.jcraft.jsch.*;
import org.apache.log4j.Logger;

import java.io.File;
import java.util.Properties;

/**
 * 描述: sftp通道
 * 版权: Copyright (c) 2016
 * 公司: 思迪科技 
 * 作者: 王功俊(wanggj@thinkive.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月08日
 * 创建时间: 16:21
 */
public class SFTPChannel
{

	private static final Logger logger = Logger.getLogger(SFTPChannel.class);

	private ChannelSftp sftpChannel;

	private Session session;

	private SFTPConfig config;

	public SFTPChannel(SFTPConfig config)
	{
		this.config = config;
		connect();
	}

	/**
	 *@描述：初始链接
	 *@作者:王功俊(wanggj@thinkive.com)
	 *@日期:2016/12/9
	 *@时间:13:59
	 */
	private void connect()
	{
		try
		{
			JSch jsch = new JSch();
			session = jsch.getSession(config.getUserName(), config.getIp(), Integer.valueOf(config.getPort()));
			logger.debug("session is created...：" + session);
			if(session != null)
			{
				session.setPassword(config.getPassword());
				Properties properties = new Properties();
				properties.put("StrictHostKeyChecking", "no");
				session.setConfig(properties); // 为Session对象设置properties
				session.setTimeout(config.getTimeout()); // 设置timeout时间
				session.connect(); // 通过Session建立链接
				logger.debug("session connected...：" + session);
				Channel channel = session.openChannel("sftp");
				channel.connect();
				sftpChannel = (ChannelSftp) channel;
				logger.info("Connected successfully to ip：" + config.getIp() + ",username：" + config.getUserName() + ",session：" + session + ",channel：" + session);
			}
		}
		catch(JSchException e)
		{
			logger.error("", e);
		}
	}

	/**
	 *@描述：上传文件夹
	 *@作者:王功俊(wanggj@thinkive.com)
	 *@日期:2016/12/9
	 *@时间:14:01
	 */
	public void putDirectory(String src, String dst, int mod)
	{
		File directory = new File(src);
		String dirName = directory.getName();
		String targetPath = dst + "/" + dirName;
		try
		{
			logger.debug("sftpChannel cd to ：" + targetPath);
			sftpChannel.cd(targetPath);
			File[] childrenFile = directory.listFiles();
			if(childrenFile != null && childrenFile.length > 0)
			{
				for(File file : childrenFile)
				{
					//当子文件是目录时，需要递归处理
					if(file.isDirectory())
					{
						putDirectory(file.getPath(), dst + "/" + dirName, mod);
					}
					else
					{
						putFile(file.getPath(), dst + "/" + dirName, mod);
					}
				}
			}
		}
		catch(SftpException e1)
		{
			//当文件不存在时，cd会抛出异常，异常的id为ChannelSftp.SSH_FX_NO_SUCH_FILE
			//此时应该创建对应的文件夹
			if(ChannelSftp.SSH_FX_NO_SUCH_FILE == e1.id)
			{
				try
				{
					logger.debug("directory not exists,sftpChannel create directory：" + targetPath);
					sftpChannel.mkdir(targetPath);
					sftpChannel.cd(targetPath);
					File[] childrenFile = directory.listFiles();
					if(childrenFile != null && childrenFile.length > 0)
					{
						for(File file : childrenFile)
						{
							if(file.isDirectory())
							{
								//当子文件是目录时，需要递归处理
								putDirectory(file.getPath(), dst + "/" + dirName, mod);
							}
							else
							{
								putFile(file.getPath(), dst + "/" + dirName, mod);
							}
						}
					}
				}
				catch(SftpException e2)
				{
					logger.error("sftpChannel create directory error：", e2);
				}
			}
		}
	}

	/**
	 *@描述：上传单个文件
	 *@作者:王功俊(wanggj@thinkive.com)
	 *@日期:2016/12/9
	 *@时间:13:59
	 */
	public void putFile(String src, String dst, int mod)
	{
		try
		{
			File file = new File(src);
			sftpChannel.put(src, dst, new SFTPProcess(file.getPath(), dst + "/" + file.getName(), file.length(), System.currentTimeMillis()), mod);
			//防止输出的格式没有换行符
			System.out.println("\n");
		}
		catch(SftpException e)
		{
			logger.error("", e);
		}
	}

	/**
	 *@描述：上传文件
	 *@作者:王功俊(wanggj@thinkive.com)
	 *@日期:2016/12/9
	 *@时间:14:20
	 */
	public void put(String src, String dst, int mod)
	{
		File srcFile = new File(src);
		if(srcFile.exists())
		{
			if(srcFile.isDirectory())
			{
				putDirectory(src, dst, mod);
			}
			else
			{
				putFile(src, dst, mod);
			}
		}
		else
		{
			logger.warn("src file is not exists...");
		}
	}

	public Channel getChannel()
	{
		return this.sftpChannel;
	}

	public void closeChannel()
	{

		if(sftpChannel != null)
		{
			logger.debug("sftp channel is close...：" + sftpChannel);
			sftpChannel.quit();
			sftpChannel.disconnect();
		}
		if(session != null)
		{
			logger.debug("session is close...：" + session);
			session.disconnect();
		}
	}
}
