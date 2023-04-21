	private void handleWriteFuture(IoSessionImpl session, WriteFuture future) throws IOException {
		
		Queue<IoFuture> writeQueue = session.getWriteQueue();
		
		ByteBuffer outBuffer = future.getBuffer();
		
		int writelen = 0;
		for(int i=0; i< 10; i++) {
			writelen += session.getChannel().write(outBuffer);
			if(outBuffer.hasRemaining()) {
				if(writelen > MAX_OUT_SIZE) {
					break;
				}
			}
			else {
				if(future.equals(writeQueue.poll())) {
					future.setComplete(null);
					break;
				}
				else{
					throw new Error("发生严重错误");
				}
			}
		}
	}
