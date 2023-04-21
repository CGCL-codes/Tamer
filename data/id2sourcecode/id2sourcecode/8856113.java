	public IoFuture connect(){
		try {
			if(isConnecting.compareAndSet(false, true)) {
				getChannel().configureBlocking(false);
				getOwnerAcceptor().scheduleToDispatcher(this);
			}
		}
		catch(IOException e) {
			connectFuture.setComplete(e);     //设置完成
		}
		return connectFuture;
	}
