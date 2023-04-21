    @Test
    public void testClientSendToChannelValidatingWrappedClientSession() throws Exception {
        final String channelName = "foo";
        final String user = "dummy";
        final String listenerName = "ValidatingChannelListener";
        DummyClient client = new DummyClient(user);
        client.connect(port).login();
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                ChannelListener listener = new ValidatingChannelListener();
                dataService.setBinding(listenerName, listener);
                ClientSession session = (ClientSession) dataService.getBinding(user);
                Channel channel = channelService.createChannel(channelName, listener, Delivery.RELIABLE);
                channel.join(session);
            }
        }, taskOwner);
        client.assertJoinedChannel(channelName);
        client.sendChannelMessage(channelName, 0);
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                ValidatingChannelListener listener = (ValidatingChannelListener) dataService.getBinding(listenerName);
                ClientSession session = (ClientSession) dataService.getBinding(user);
                listener.validateSession(session);
                System.err.println("sessions are equal");
            }
        }, taskOwner);
    }
