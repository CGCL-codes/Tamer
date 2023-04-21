    protected void joinUsers(final String channelName, final String... users) throws Exception {
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                for (String user : users) {
                    ClientSession session = (ClientSession) dataService.getBinding(user);
                    channel.join(session);
                }
            }
        }, taskOwner);
    }
