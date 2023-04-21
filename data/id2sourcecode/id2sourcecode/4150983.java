                public void run() {
                    Channel channel = getChannel(channelName);
                    channel.send(null, ByteBuffer.wrap(buf.getBuffer()));
                }
