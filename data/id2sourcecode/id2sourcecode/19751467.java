        public void preShowChange() {
            Channels channels = context.getShow().getChannels();
            for (Channel channel : channels) {
                channel.removeNameListener(channelNameListener);
            }
        }
