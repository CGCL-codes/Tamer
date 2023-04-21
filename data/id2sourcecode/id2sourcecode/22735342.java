    private void resubscribe_if_needed() {
        Enumeration channel_names = EventConsumer.getChannelMap().keys();
        long now = System.currentTimeMillis();
        while (channel_names.hasMoreElements()) {
            String name = (String) channel_names.nextElement();
            EventChannelStruct eventChannelStruct = EventConsumer.getChannelMap().get(name);
            if ((now - eventChannelStruct.last_subscribed) > EVENT_RESUBSCRIBE_PERIOD / 3) {
                reSubscribeByName(eventChannelStruct, name);
            }
            eventChannelStruct.consumer.checkIfHeartbeatSkipped(name, eventChannelStruct);
        }
    }
