                public void run() {
                    Channel channel = getChannel(channelName);
                    if (getChannel(channelName) != null) {
                        fail("obtained closed channel");
                    }
                }
