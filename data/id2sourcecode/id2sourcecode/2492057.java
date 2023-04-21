                public void run() {
                    Channel channel = getChannel(channelName);
                    int numJoinedSessions = getSessions(channel).size();
                    if (numJoinedSessions != 0) {
                        fail("Expected no sessions, got " + numJoinedSessions);
                    }
                    System.err.println("All sessions left");
                    dataService.removeObject(channel);
                }
