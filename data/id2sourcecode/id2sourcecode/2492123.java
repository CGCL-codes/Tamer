    private void closeChannel(final String name) throws Exception {
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = channelService.getChannel(name);
                dataService.removeObject(channel);
            }
        }, taskOwner);
    }
