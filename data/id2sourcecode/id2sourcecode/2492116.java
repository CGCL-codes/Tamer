        public void receivedMessage(Channel channel, ClientSession session, ByteBuffer message) {
            if (name != null) {
                assertEquals(channel, AppContext.getChannelManager().getChannel(name));
            }
            if (allowMessages) {
                channel.send(session, message);
            }
        }
