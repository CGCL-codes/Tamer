    @Test
    @IntegrationTest
    public void testChannelLeaveSessionNotJoined() throws Exception {
        final String channelName = "leaveTest";
        createChannel(channelName);
        ClientGroup group = new ClientGroup(someUsers);
        try {
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = getChannel(channelName);
                    ClientSession moe = (ClientSession) dataService.getBinding(MOE);
                    channel.join(moe);
                    try {
                        ClientSession larry = (ClientSession) dataService.getBinding(LARRY);
                        channel.leave(larry);
                        System.err.println("leave of non-member session returned");
                    } catch (Exception e) {
                        System.err.println(e);
                        fail("test failed with exception: " + e);
                    }
                }
            }, taskOwner);
            Thread.sleep(100);
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = getChannel(channelName);
                    ClientSession moe = (ClientSession) dataService.getBinding(MOE);
                    ClientSession larry = (ClientSession) dataService.getBinding(LARRY);
                    Set<ClientSession> sessions = getSessions(channel);
                    System.err.println("sessions set (should only have moe): " + sessions);
                    if (sessions.size() != 1) {
                        fail("Expected 1 session, got " + sessions.size());
                    }
                    if (!sessions.contains(moe)) {
                        fail("Expected session: " + moe);
                    }
                    dataService.removeObject(channel);
                }
            }, taskOwner);
        } finally {
            group.disconnect(false);
        }
    }
