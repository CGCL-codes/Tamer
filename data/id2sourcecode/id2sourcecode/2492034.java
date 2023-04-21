                public void run() {
                    Channel channel = channelService.getChannel(channelName);
                    List<String> users = new ArrayList<String>(Arrays.asList(someUsers));
                    Iterator<ClientSession> iter = channel.getSessions();
                    while (iter.hasNext()) {
                        ClientSession session = iter.next();
                        if (!(session instanceof ClientSessionWrapper)) {
                            fail("session not ClientSessionWrapper instance: " + session);
                        }
                        String name = session.getName();
                        if (!users.contains(name)) {
                            fail("unexpected channel member: " + name);
                        } else {
                            System.err.println("getSessions includes: " + name);
                            users.remove(name);
                        }
                    }
                    if (!users.isEmpty()) {
                        fail("Expected getSessions to include: " + users);
                    }
                }
