package com.truncate.sftp;

import com.jcraft.jsch.SftpProgressMonitor;

/**
 * 描述: 传输进度条
 * 版权: Copyright (c) 2016
 * 作者: truncate(wy940407@163.com)
 * 版本: 1.0 
 * 创建日期: 2016年12月09日
 * 创建时间: 11:24
 */
public class SFTPProcess implements SftpProgressMonitor
{

	private long fileSize;

	private long startTime;

	private String filePath;

	private String targetPath;

	private long totalBytesTransferred = 0;

	public SFTPProcess(String filePath, String targetPath, long fileSize, long startTime)
	{
		this.fileSize = fileSize;
		this.startTime = startTime;
		this.filePath = filePath;
		this.targetPath = targetPath;
	}

	public void init(int i, String s, String s1, long l)
	{

	}

	public boolean count(long count)
	{
		totalBytesTransferred += count;
		long end_time = System.currentTimeMillis();
		long time = (end_time - startTime) / 1000; // 耗时多长时间
		long speed; // 传输速度
		if(0 == time)
		{
			speed = 0;
		}
		else
		{
			speed = totalBytesTransferred / 1024 / time;
		}
		System.out.printf("\r  %s -->  %s	%d%%   %d  %dKB/s  %d   %ds", filePath, targetPath, totalBytesTransferred * 100 / fileSize, totalBytesTransferred, speed, fileSize, time);
		return true;
	}

	public void end()
	{

	}
}
