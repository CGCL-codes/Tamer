        public void onReplyTopic(String channel, String topic) {
            getChannel(channel, true).getChannelMux().onReplyTopic(channel, topic);
        }
