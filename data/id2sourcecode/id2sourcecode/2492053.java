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
