	private void registerIoSessionNow(IoSessionImpl session) throws IOException {
		SocketChannel channel = session.getChannel();
		if(channel == null) {
			throw new Error("通道不能为空");
		}

		/*if(channel.isRegistered()) {
			throw new RuntimeException("此通道已经主册过");
		}*/

		
		channel.configureBlocking(false);                //设置为非阻塞
		
		if(session.getClass() == IoSessionImpl.class) {
			//注册
			SelectionKey selectKey = channel.register(selector, SelectionKey.OP_READ, session);  //向选择器注册通道

			session.setSelectionKey(selectKey);   //设置会话的选择键

			//session.setOwnerDispatcher(this);    //设置归属IO处理器

			this.onRegisterSession(session);
		}
		
		else if(session.getClass() == ClientIoSession.class) {  //注册连接操作
			ClientIoSession clientIoSession = (ClientIoSession) session;
			SelectionKey selectKey = channel.register(selector, SelectionKey.OP_CONNECT, session);
			channel.connect(clientIoSession.getConnectAddress());
			session.setSelectionKey(selectKey);
		}
		
		//开启定时检查
		session.lastAccessTime = System.currentTimeMillis();
		session.lastReadTime = System.currentTimeMillis();
		session.lastWriteTime = System.currentTimeMillis();

		session.getOwnerAcceptor().getTimer().schedule(session.getCheckOverTime(), 
				session.getOwnerAcceptor().getConfigure().getOverTimeIntervalCheckTime(), 
				session.getOwnerAcceptor().getConfigure().getOverTimeIntervalCheckTime());

	}
