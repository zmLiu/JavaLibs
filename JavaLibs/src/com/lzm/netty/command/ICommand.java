package com.lzm.netty.command;

import io.netty.channel.ChannelHandlerContext;

public interface ICommand {
	/**
	 * ��ȡ�����
	 * */
	int getCmd();
	
	/**
	 * ���ܵ���Ϣ
	 * */
	void execute(ChannelHandlerContext ctx,String []msgs) throws Exception;
}
