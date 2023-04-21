    protected void leaveAll(final String channelName) throws Exception {
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                channel.leaveAll();
            }
        }, taskOwner);
    }
