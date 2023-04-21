    private void checkUsersJoined0(final String channelName, final String... users) throws Exception {
        txnScheduler.runTask(new TestAbstractKernelRunnable() {

            public void run() {
                Channel channel = getChannel(channelName);
                Set<ClientSession> sessions = getSessions(channel);
                System.err.println("Sessions joined:" + sessions);
                if (sessions.size() != users.length) {
                    fail("Expected " + users.length + " sessions, got " + sessions.size());
                }
                List<String> userList = Arrays.asList(users);
                for (ClientSession session : sessions) {
                    if (!userList.contains(session.getName())) {
                        fail("Expected session: " + session);
                    }
                }
            }
        }, taskOwner);
    }
