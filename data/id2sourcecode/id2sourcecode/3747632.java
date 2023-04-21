	public void handleConnect(IoSessionImpl session) throws IOException{
		if(session.getChannel().finishConnect()) {
			session.setReadControl(true);         //打开读
			session.setWriteControl(true);        //打开与
			session.setConnectControl(false);     //关闭
			this.onRegisterSession(session);      //session添加到管理器中

			ConnectFuture future =  (ConnectFuture) ((ClientIoSession) session).getConnectFuture();
			future.setComplete(null);                 //设置完成
		}
	}
