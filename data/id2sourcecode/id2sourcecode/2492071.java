            public void run() {
                Channel channel = getChannel(channelName);
                try {
                    channel.send(null, null);
                    fail("Expected NullPointerException");
                } catch (NullPointerException e) {
                    System.err.println(e);
                }
            }
