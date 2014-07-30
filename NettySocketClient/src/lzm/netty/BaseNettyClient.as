package lzm.netty
{
	import flash.events.EventDispatcher;
	import flash.utils.ByteArray;
	
	import lzm.netty.socket.NettySocket;
	import lzm.netty.socket.SocketEvent;
	
	[Event(name="NettySocket_Connect",type="lzm.netty.socket.SocketEvent")]
	[Event(name="NettySocket_Close",type="lzm.netty.socket.SocketEvent")]
	[Event(name="NettySocket_Data",type="lzm.netty.socket.SocketEvent")]
	[Event(name="NettySocket_Security_Error",type="lzm.netty.socket.SocketEvent")]
	[Event(name="NettySocket_IO_Error",type="lzm.netty.socket.SocketEvent")]

	public class BaseNettyClient extends EventDispatcher
	{
		
		protected var _host:String;
		protected var _port:int;
		protected var _socket:NettySocket;
		
		protected var _isConnect:Boolean = false;
		
		protected var _commands:Object;
		
		public function BaseNettyClient(host:String,port:int){
			_host = host;
			_port = port;
			_socket = new NettySocket();
			_commands = new Object();
		}
		
		public function connect():void{
			_socket.connect(_host,_port);
			addEventListeners();
		}
		
		public function close():void{
			_socket.close();
		}
		
		protected function addEventListeners():void{
			_socket.addEventListener(SocketEvent.CONNECT,onConnect);
			_socket.addEventListener(SocketEvent.CLOSE,onClose);
			_socket.addEventListener(SocketEvent.DATA,onData);
			_socket.addEventListener(SocketEvent.SECURITY_ERROR,onSecurityError);
			_socket.addEventListener(SocketEvent.IO_ERROR,onIOError);
		}
		
		protected function removeEventListeners():void{
			_socket.removeEventListener(SocketEvent.CONNECT,onConnect);
			_socket.removeEventListener(SocketEvent.CLOSE,onClose);
			_socket.removeEventListener(SocketEvent.DATA,onData);
			_socket.removeEventListener(SocketEvent.SECURITY_ERROR,onSecurityError);
			_socket.removeEventListener(SocketEvent.IO_ERROR,onIOError);
		}
		
		/**
		 * 连接成功
		 * */
		protected function onConnect(e:SocketEvent):void{
			_isConnect = true;
			dispatchEvent(new SocketEvent(SocketEvent.CONNECT));
		}
		
		/**
		 * 连接关闭
		 * */
		protected function onClose(e:SocketEvent):void{
			_isConnect = false;
			removeEventListeners();
			dispatchEvent(new SocketEvent(SocketEvent.CLOSE));
		}
		
		/**
		 * 接收到数据
		 * */
		protected function onData(e:SocketEvent):void{
			
		}
		
		/**
		 * 派发逻辑命令
		 * */
		protected function dispatchDatas(cmd:String,msgs:Object):void{
			var vector:Vector.<Function> = _commands[cmd];
			if(vector){
				var length:int = vector.length;
				for (var i:int = 0; i < length; i++) {
					vector[i](msgs);
				}
			}
		}
		
		/**
		 * 数据解析数据
		 * */
		protected function parseData(data:ByteArray):Object{
			return null;
		}
		
		/**
		 * 沙箱错误
		 * */
		protected function onSecurityError(e:SocketEvent):void{
			removeEventListeners();
			dispatchEvent(new SocketEvent(SocketEvent.SECURITY_ERROR));
		}
		
		/**
		 * io错误
		 * */
		protected function onIOError(e:SocketEvent):void{
			removeEventListeners();
			dispatchEvent(new SocketEvent(SocketEvent.IO_ERROR));
		}
		
		
		/**
		 * 注册命令 
		 * @param commandId 命令的id
		 * @param callBack	接受到该命令的回调
		 */
		public function registerCommand(commandId:String,callBack:Function):void{
			var vector:Vector.<Function> = _commands[commandId];
			if(vector == null){
				vector = new Vector.<Function>();
			}
			vector.push(callBack);
			_commands[commandId] = vector;			
		}
		
		/**
		 * 注销命令
		 */		
		public function removeCommand(commandId:String,callBack:Function=null):void{
			if(callBack == null){
				delete _commands[commandId];
			}else{
				var vector:Vector.<Function> = _commands[commandId];
				if(vector != null){
					var length:int = vector.length;
					for (var i:int = 0; i < length; i++) {
						if(vector[i] == callBack){
							vector.splice(i,1);
							break;
						}
					}
				}
			}
		}
		
		/**
		 * 发送消息
		 * */
		public function sendMessages(cmd:String,msgs:Object):void{
			
		}
		
		/**
		 * 是否连接
		 * */
		public function get isConnect():Boolean{
			return _isConnect;
		}
		
	}
}