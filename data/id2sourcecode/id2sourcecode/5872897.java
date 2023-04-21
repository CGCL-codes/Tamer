    public void run() {
        while (isRunning()) {
            setBusy(true);
            ChannelQueue queue = ChannelQueue.getInstance();
            BroadcastInfo bi = null;
            while ((bi = queue.receive()) != null) {
                Channel channel = bi.getChannel();
                channel.broadcast(bi);
            }
            setBusy(false);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                while (Thread.interrupted()) {
                }
            }
        }
    }
