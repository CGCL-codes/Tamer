                    public void run() {
                        Channel channel = getChannel(channelName);
                        ClientSession session = getSession(user);
                        if (getSessions(channel).contains(session)) {
                            fail("Failed to remove session: " + session);
                        }
                    }
