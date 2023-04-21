    @Test
    public void testChannelLeaveClosedChannel() throws Exception {
        final String channelName = "leaveClosedChannelTest";
        final String user = "daffy";
        createChannel(channelName);
        ClientGroup group = new ClientGroup(user);
        try {
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = getChannel(channelName);
                    ClientSession session = (ClientSession) dataService.getBinding(user);
                    channel.join(session);
                    dataService.removeObject(channel);
                    try {
                        channel.leave(session);
                        fail("Expected IllegalStateException");
                    } catch (IllegalStateException e) {
                        System.err.println(e);
                    }
                }
            }, taskOwner);
        } finally {
            group.disconnect(false);
        }
    }
