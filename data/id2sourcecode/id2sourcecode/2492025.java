                public void run() {
                    Channel channel = channelService.getChannel(channelName);
                    if (!channel.hasSessions()) {
                        fail("Expected sessions joined");
                    }
                }
