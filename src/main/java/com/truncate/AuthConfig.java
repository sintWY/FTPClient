package com.truncate;

import com.truncate.sftp.SFTPConstant;
import org.apache.log4j.Logger;

/**
 * 描述: //TODO 类描述
 * 版权: Copyright (c) 2016
 * 公司: 思迪科技 
 * 作者: 王功俊(wanggj@thinkive.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月08日
 * 创建时间: 16:09
 */
public class AuthConfig
{

	private static final Logger logger = Logger.getLogger(AuthConfig.class);

	//ip地址
	private String ip;

	//端口号
	private String port;

	//用户名
	private String userName;

	//密码
	private String password;

	//超时时间
	private int timeout;

	public AuthConfig(String ip, String port, String userName, String password, int timeout)
	{
		this.ip = ip;
		this.port = port;
		this.userName = userName;
		this.password = password;
		this.timeout = timeout;
	}

	public AuthConfig(String ip, String port, String userName, String password)
	{
		this(ip, port, userName, password, SFTPConstant.DEFAULT_TIME_OUT);
	}

	public String getIp()
	{
		return ip;
	}

	public String getPort()
	{
		return port;
	}

	public String getUserName()
	{
		return userName;
	}

	public String getPassword()
	{
		return password;
	}

	public int getTimeout()
	{
		return timeout;
	}

}
