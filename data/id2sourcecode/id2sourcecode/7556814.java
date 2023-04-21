        public void onTopic(String channel, String topic) {
            getChannel(channel, true).getChannelMux().onTopic(channel, topic);
        }
