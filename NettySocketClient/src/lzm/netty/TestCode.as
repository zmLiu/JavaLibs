package lzm.netty
{
	import flash.utils.setTimeout;

	public class TestCode
	{
		public function TestCode()
		{
			var socketClient:JsonNettyClient = new JsonNettyClient("127.0.0.1",9002);
			socketClient.registerCommand("test",function(json:Object):void{
				trace(JSON.stringify(json));
			});
			socketClient.onConnectFun = function():void{
				trace("链接成功");
				setTimeout(function():void{
					socketClient.sendMessages("test",{"c":"test"});
				},1000);
			}
			socketClient.onCloseFun = function():void{
				trace("链接关闭");
			}
			socketClient.connect();
		}
	}
}