                    public void run() {
                        Channel channel = getChannel(channelName);
                        ClientSession session = getSession(user);
                        channel.leave(session);
                    }
