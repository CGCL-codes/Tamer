		@Override
		public void run() {
			
			
			
			long currTime = System.currentTimeMillis();
			long bothOverTime = ownerAcceptor.getConfigure().getBothOverTime();
			long readOverTime = ownerAcceptor.getConfigure().getReadOverTime();
			long writeOverTime = ownerAcceptor.getConfigure().getWriteOverTime();
			
			if(bothOverTime <=0 || readOverTime <= 0 || writeOverTime <=0 ) {
				//TODO:不存在超时处理
				return;
			}
			
			
			//检查是否有超时产生，有的话就排序到IO处理线程中去
			if((currTime - lastAccessTime) > bothOverTime ||
					(currTime - lastReadTime) > readOverTime ||
					(currTime - lastWriteTime) > writeOverTime) {
				if(!IoSessionImpl.this.isCloseing() || !IoSessionImpl.this.isClose()) {
					if(isOverTimeHandleing.compareAndSet(false, true)) {
						ownerDispatcher.scheduleOverTime(IoSessionImpl.this); //排程	
					}
				}
			}
		}
