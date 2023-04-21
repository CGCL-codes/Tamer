    @Test
    @IntegrationTest
    public void testChannelLeave() throws Exception {
        final String channelName = "leaveTest";
        createChannel(channelName);
        ClientGroup group = new ClientGroup(someUsers);
        try {
            Thread.sleep(1000);
            int count = getObjectCount();
            joinUsers(channelName, someUsers);
            checkUsersJoined(channelName, someUsers);
            for (final String user : someUsers) {
                txnScheduler.runTask(new TestAbstractKernelRunnable() {

                    public void run() {
                        Channel channel = getChannel(channelName);
                        ClientSession session = getSession(user);
                        channel.leave(session);
                    }
                }, taskOwner);
                Thread.sleep(100);
                txnScheduler.runTask(new TestAbstractKernelRunnable() {

                    public void run() {
                        Channel channel = getChannel(channelName);
                        ClientSession session = getSession(user);
                        if (getSessions(channel).contains(session)) {
                            fail("Failed to remove session: " + session);
                        }
                    }
                }, taskOwner);
            }
            Thread.sleep(1000);
            assertEquals(count, getObjectCount());
            txnScheduler.runTask(new TestAbstractKernelRunnable() {

                public void run() {
                    Channel channel = getChannel(channelName);
                    int numJoinedSessions = getSessions(channel).size();
                    if (numJoinedSessions != 0) {
                        fail("Expected no sessions, got " + numJoinedSessions);
                    }
                    System.err.println("All sessions left");
                    dataService.removeObject(channel);
                }
            }, taskOwner);
        } finally {
            group.disconnect(false);
        }
    }
