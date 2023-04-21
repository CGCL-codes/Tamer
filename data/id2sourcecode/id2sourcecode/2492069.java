            public void run() {
                Channel channel = getChannel(channelName);
                dataService.removeObject(channel);
                try {
                    channel.send(null, ByteBuffer.wrap(testMessage));
                    fail("Expected IllegalStateException");
                } catch (IllegalStateException e) {
                    System.err.println(e);
                }
            }
