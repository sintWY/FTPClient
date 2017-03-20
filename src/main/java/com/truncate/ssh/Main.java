package com.truncate.ssh;

import com.truncate.AuthConfig;
import org.apache.log4j.Logger;

/**
 * 描述: //TODO 类描述
 * 版权: Copyright (c) 2016
 * 公司: 思迪科技 
 * 作者: 王功俊(wanggj@thinkive.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月09日
 * 创建时间: 16:44
 */
public class Main
{

	private static Logger logger = Logger.getLogger(Main.class);

	public static void main(String[] args)
	{
		if(args != null && args.length > 0)
		{
			ExecuterChannel channel = null;
			try
			{
				String port = "22";//默认写死
				String ip = args[0];
				String userName = args[1];
				String password = args[2];
				logger.info("input params：ip=" + ip + ",port=" + port + ",userName=" + userName + ",password=" + password);
				AuthConfig config = new AuthConfig(ip, port, userName, password);
				channel = new ExecuterChannel(config);
				channel.execute("cd /data/srv");
				String result = channel.execute("ll");
				logger.info("result：" + result);
			}
			catch(Exception e)
			{
				logger.error("", e);
			}
		}
		else
		{
			logger.warn("请检查输入的参数.");
		}
	}
}
