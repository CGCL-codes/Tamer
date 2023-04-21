    private void testChannelCloseWithManagedChanneListener(final boolean removeListener) throws Exception {
        final String channelName = "closeTest";
        final String listenerName = channelName + ".listener";
        int count = getObjectCount();
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                ManagedChannelListener listener = new ManagedChannelListener();
                Channel channel = AppContext.getChannelManager().createChannel(channelName, listener, Delivery.RELIABLE);
                DataManager dataManager = AppContext.getDataManager();
                dataManager.setBinding(channelName, channel);
                dataManager.setBinding(listenerName, listener);
            }
        }, taskOwner);
        printServiceBindings("after channel create");
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                DataManager dataManager = AppContext.getDataManager();
                ManagedChannelListener listener = (ManagedChannelListener) dataManager.getBinding(listenerName);
                Channel channel = (Channel) dataManager.getBinding(channelName);
                dataManager.removeBinding(listenerName);
                if (removeListener) {
                    dataManager.removeObject(listener);
                }
                dataManager.removeObject(channel);
            }
        }, taskOwner);
        Thread.sleep(100);
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                if (getChannel(channelName) != null) {
                    fail("obtained closed channel");
                }
            }
        }, taskOwner);
        printServiceBindings("after channel close");
        assertEquals(count + (removeListener ? 0 : 1), getObjectCount());
    }
