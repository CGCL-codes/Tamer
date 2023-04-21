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
