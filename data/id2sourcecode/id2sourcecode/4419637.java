    public IRCEvent createEvent(IRCEvent event) {
        if (event.numeric() == 332) {
            TopicEvent tEvent = new TopicEvent(event.getRawEventData(), event.getSession(), event.getSession().getChannel(event.arg(1)), event.arg(2));
            if (topicMap.containsValue(tEvent.getChannel())) {
                topicMap.get(tEvent.getChannel()).appendToTopic(tEvent.getTopic());
            } else {
                topicMap.put(tEvent.getChannel(), tEvent);
            }
        } else {
            Channel chan = event.getSession().getChannel(event.arg(1));
            if (topicMap.containsKey(chan)) {
                TopicEvent tEvent = (TopicEvent) topicMap.get(chan);
                topicMap.remove(chan);
                tEvent.setSetBy(event.arg(2));
                tEvent.setSetWhen(event.arg(3));
                chan.setTopicEvent(tEvent);
                return tEvent;
            }
        }
        return event;
    }
