package com.truncate.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;
import org.apache.log4j.Logger;

/**
 * 描述: 应用启动类
 * 版权: Copyright (c) 2016
 * 公司: 思迪科技 
 * 作者: 王功俊(wanggj@thinkive.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月08日
 * 创建时间: 16:03
 */
public class Main
{

	private static Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args) throws SftpException
	{
		if(args != null && args.length > 0)
		{
			SFTPChannel channel = null;
			try
			{
				String port = "22";//默认写死
				String ip = args[0];
				String userName = args[1];
				String password = args[2];
				String src = args[3];
				String dst = args[4];
				logger.info("input params：ip=" + ip + ",port=" + port + ",userName=" + userName + ",password=" + password + ",src=" + src + ",dst=" + dst);
				SFTPConfig config = new SFTPConfig(ip, port, userName, password);
				channel = new SFTPChannel(config);
				channel.put(src, dst, ChannelSftp.OVERWRITE);
			}
			catch(Exception e)
			{
				logger.error("", e);
			}
			finally
			{
				if(channel != null)
				{
					channel.closeChannel();
				}
			}
		}
		else
		{
			logger.warn("请检查输入的参数.");
		}
	}
}
