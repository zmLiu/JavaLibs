package com.lzm.netty
{
	import com.lzm.netty.socket.NettySocket;
	import com.lzm.netty.socket.Packet;
	import com.lzm.netty.socket.SocketEvent;
	
	import flash.utils.ByteArray;
	

	public class BaseNettyClient
	{
		
		protected var _host:String;
		protected var _port:int;
		protected var _socket:NettySocket;
		
		protected var _isConnect:Boolean = false;
		
		protected var _commands:Object;
		
		public var onConnectFun:Function;//连接成功的回掉方法
		public var onCloseFun:Function;//连接关闭的回掉方法
		
		public function BaseNettyClient(host:String,port:int){
			_host = host;
			_port = port;
			_socket = new NettySocket();
			_commands = new Object();
		}
		
		public function connect():void{
			_socket.connect(_host,_port);
			
			_socket.addEventListener(SocketEvent.CONNECT,onConnect);
			_socket.addEventListener(SocketEvent.CLOSE,onClose);
			_socket.addEventListener(SocketEvent.DATA,onData);
			_socket.addEventListener(SocketEvent.SECURITY_ERROR,onSecurityError);
			_socket.addEventListener(SocketEvent.IO_ERROR,onIOError);
		}
		
		/**
		 * 连接成功
		 * */
		protected function onConnect(e:SocketEvent):void{
			_isConnect = true;
			if(onConnectFun) onConnectFun();
		}
		
		/**
		 * 连接关闭
		 * */
		protected function onClose(e:SocketEvent):void{
			_isConnect = false;
			
			_socket.removeEventListener(SocketEvent.CONNECT,onConnect);
			_socket.removeEventListener(SocketEvent.CLOSE,onClose);
			_socket.removeEventListener(SocketEvent.DATA,onData);
			_socket.removeEventListener(SocketEvent.SECURITY_ERROR,onSecurityError);
			_socket.removeEventListener(SocketEvent.IO_ERROR,onIOError);
			
			if(onCloseFun) onCloseFun();
		}
		
		/**
		 * 接收到数据
		 * */
		protected function onData(e:SocketEvent):void{
			var data:Packet = e.data;
			var commandId:int = data.readShort();
			var msgs:Object = parseData(data);
			dispatchDatas(commandId,msgs);
		}
		
		/**
		 * 派发逻辑命令
		 * */
		protected function dispatchDatas(cmd:int,msgs:Object):void{
			var vector:Vector.<Function> = _commands[cmd];
			if(vector){
				var length:int = vector.length;
				for (var i:int = 0; i < length; i++) {
					vector[i](msgs);
				}
			}
		}
		
		/**
		 * 数据解析为数组
		 * */
		protected function parseData(data:Packet):Object{
			var msgs:Array = [];
			var length:int = data.readShort();
			for (var i:int = 0; i < length; i++) {
				msgs.push(data.readString());
			}
			return msgs;
		}
		
		/**
		 * 沙箱错误
		 * */
		protected function onSecurityError(e:SocketEvent):void{}
		
		/**
		 * io错误
		 * */
		protected function onIOError(e:SocketEvent):void{}
		
		
		/**
		 * 注册命令 
		 * @param commandId 命令的id
		 * @param callBack	接受到该命令的回调
		 */
		public function registerCommand(commandId:int,callBack:Function):void{
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
		public function removeCommand(commandId:int,callBack:Function=null):void{
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
		public function sendMessages(cmd:int,msgs:Object):void{
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
		
		/**
		 * 是否连接
		 * */
		public function get isConnect():Boolean{
			return _isConnect;
		}
		
	}
}