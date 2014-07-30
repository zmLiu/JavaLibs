package lzm.netty
{
	import flash.utils.setTimeout;
	
	import lzm.netty.socket.SocketEvent;

	public class TestCode
	{
		public function TestCode()
		{
			var socketClient:JsonNettyClient = new JsonNettyClient("127.0.0.1",9002);
			socketClient.registerCommand("test",function(json:Object):void{
				trace(JSON.stringify(json));
			});
			socketClient.addEventListener(SocketEvent.CONNECT,function(e:SocketEvent):void{
				trace("链接成功");
				setTimeout(function():void{
					socketClient.sendMessages("test",{"c":"test"});
				},1000);
			});
			socketClient.addEventListener(SocketEvent.CLOSE,function(e:SocketEvent):void{
				trace("链接关闭");
			});
			socketClient.connect();
		}
	}
}