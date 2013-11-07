package com.lzm.netty
{
	import com.lzm.netty.socket.Packet;
	import com.lzm.netty.socket.SocketEvent;
	
	import flash.utils.ByteArray;

	public class StringArrayNettyClient extends BaseNettyClient
	{
		public function StringArrayNettyClient(host:String, port:int)
		{
			super(host, port);
		}
		
		/**
		 * 接收到数据
		 * */
		protected override function onData(e:SocketEvent):void{
			var data:Packet = e.data;
			var cmd:int = data.readShort();
			var msgs:Object = parseData(data);
			dispatchDatas(cmd,msgs);
		}
		
		protected override function parseData(data:Packet):Object{
			var msgs:Array = [];
			var length:int = data.readShort();
			for (var i:int = 0; i < length; i++) {
				msgs.push(data.readString());
			}
			return msgs;
		}
		
		/**
		 * 发送消息
		 * */
		public override function sendMessages(cmd:int,msgs:Object):void{
			var data:Packet = new Packet();
			var length:int = msgs.length;
			
			data.writeShort(cmd);
			data.writeShort(length);
			
			for (var i:int = 0; i < length; i++) {
				data.writeString(msgs[i]);
			}
			
			var sendBytes:ByteArray = new ByteArray();
			sendBytes.writeShort(data.array().length);
			sendBytes.writeBytes(data.array());
			
			_socket.writeBytes(sendBytes);
			_socket.flush();
		}
		
	}
}