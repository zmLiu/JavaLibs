package lzm.netty
{
	import flash.utils.ByteArray;
	
	import lzm.netty.socket.SocketEvent;

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
			var msgs:Object = parseData(e.data);
			var cmd:String = msgs["cmd"];
			dispatchDatas(cmd,msgs);
		}
		
		protected override function parseData(data:ByteArray):Object{
			var object:Object = JSON.parse(data.readMultiByte(data.length,"utf-8"));
			return object;
		}
		
		/**
		 * 发送消息
		 * */
		public override function sendMessages(cmd:String,msgs:Object):void{
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