    @Test
    public void testChannelHasSessionsWithSessionsJoined() throws Exception {
        final String channelName = "foo";
        createChannel(channelName);
        ClientGroup group = new ClientGroup(someUsers);
        try {
            joinUsers("foo", someUsers);
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = channelService.getChannel(channelName);
                    if (!channel.hasSessions()) {
                        fail("Expected sessions joined");
                    }
                }
            }, taskOwner);
        } finally {
            group.disconnect(false);
        }
    }
