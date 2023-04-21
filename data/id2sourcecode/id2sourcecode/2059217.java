	public void init(int readwriteThreadNum) throws Exception {
		
		//如果先前存在，则先停止
		if(ioDispatchers != null) {
			for(int i=0; i<ioDispatchers.length; i++) {
				ioDispatchers[i].close();
			}
		}

		ioDispatchers = new IoReadWriteMachine[readwriteThreadNum];
		for(int i=0; i<readwriteThreadNum; i++) {
			ioDispatchers[i] = new IoReadWriteMachine();
			ioDispatchers[i].init(2000);
			ioDispatchers[i].addListener(ioAcceptor);
		}
	}
