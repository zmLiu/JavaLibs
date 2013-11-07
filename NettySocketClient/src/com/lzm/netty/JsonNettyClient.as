package com.lzm.netty
{
	import com.lzm.netty.socket.Packet;
	import com.lzm.netty.socket.SocketEvent;
	
	import flash.utils.ByteArray;

	public class JsonNettyClient extends BaseNettyClient
	{
		public function JsonNettyClient(host:String, port:int)
		{
			super(host, port);
		}
		
		/**
		 * 接收到数据
		 * */
		protected override function onData(e:SocketEvent):void{
			var data:Packet = e.data;
			var msgs:Object = parseData(data);
			var cmd:int = msgs["cmd"];
			dispatchDatas(cmd,msgs);
		}
		
		protected override function parseData(data:Packet):Object{
			var bytes:ByteArray = data.array();
			var object:Object = JSON.parse(bytes.readMultiByte(bytes.length,"utf-8"));
			return object;
		}
		
		/**
		 * 发送消息
		 * */
		public override function sendMessages(cmd:int,msgs:Object):void{
			msgs["cmd"] = cmd;
			
			var bytes:ByteArray = new ByteArray();
			bytes.writeMultiByte(JSON.stringify(msgs),"utf-8");
			
			var sendBytes:ByteArray = new ByteArray();
			sendBytes.writeShort(bytes.length);
			sendBytes.writeBytes(bytes);
			
			_socket.writeBytes(sendBytes);
			_socket.flush();
		}
		
	}
}