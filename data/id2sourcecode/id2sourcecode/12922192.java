        public ClientChannelListener joinedChannel(ClientChannel clientChannel) {
            channels.put(clientChannel.getName(), clientChannel);
            getChannel(clientChannel.getName());
            engineListener.channelJoined(player, clientChannel.getName());
            return this;
        }
