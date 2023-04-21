    public TcpChannel getChannel() throws InterruptedException {
        return channelQueue.take();
    }
