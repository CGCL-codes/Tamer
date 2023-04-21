    @Override
    public void onTopic(TopicEvent<CubeIRC> event) throws Exception {
        MessageQueue.addQueue(MessageQueueEnum.CHANNEL_TOPIC, new ChannelTopicResponse(event.getChannel(), event.getTopic(), event.getUser().getNick(), event.getDate()));
        super.onTopic(event);
    }
