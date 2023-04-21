    @Test
    public void testChannelSendAllClosedChannel() throws Exception {
        final String channelName = "test";
        createChannel(channelName);
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                dataService.removeObject(channel);
                try {
                    channel.send(null, ByteBuffer.wrap(testMessage));
                    fail("Expected IllegalStateException");
                } catch (IllegalStateException e) {
                    System.err.println(e);
                }
            }
        }, taskOwner);
    }
