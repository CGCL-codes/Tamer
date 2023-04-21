        public void postShowChange() {
            Channels channels = context.getShow().getChannels();
            for (Channel channel : channels) {
                channel.addNameListener(channelNameListener);
            }
            updateChannels();
        }
