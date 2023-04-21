                public void run() {
                    Channel channel = getChannel(channelName);
                    ClientSession moe = (ClientSession) dataService.getBinding(MOE);
                    channel.join(moe);
                    try {
                        ClientSession larry = (ClientSession) dataService.getBinding(LARRY);
                        channel.leave(larry);
                        System.err.println("leave of non-member session returned");
                    } catch (Exception e) {
                        System.err.println(e);
                        fail("test failed with exception: " + e);
                    }
                }
