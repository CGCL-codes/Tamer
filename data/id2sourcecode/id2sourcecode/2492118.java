        public void receivedMessage(Channel channel, ClientSession session, ByteBuffer message) {
            if (name != null) {
                assertEquals(channel, AppContext.getChannelManager().getChannel(name));
            }
            if (message.getInt() % 2 == 0) {
                message.flip();
                channel.send(session, message);
            }
        }
