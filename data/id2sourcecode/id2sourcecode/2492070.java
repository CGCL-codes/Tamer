    @Test
    public void testChannelSendNullMessage() throws Exception {
        final String channelName = "test";
        createChannel(channelName);
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                try {
                    channel.send(null, null);
                    fail("Expected NullPointerException");
                } catch (NullPointerException e) {
                    System.err.println(e);
                }
            }
        }, taskOwner);
    }
