    private void firstPartOfTopic(String data, Connection con) {
        TopicEvent tEvent = IRCEventFactory.topic(data, con);
        if (topicMap.containsValue(tEvent.getChannel())) {
            ((TopicEventImpl) topicMap.get(tEvent.getChannel())).appendToTopic(tEvent.getTopic());
        } else {
            topicMap.put(tEvent.getChannel(), tEvent);
        }
    }
