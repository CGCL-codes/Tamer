    private void secondPartOfTopic(String data, Connection con) {
        Pattern p = Pattern.compile(":(\\S+)\\s+333\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+)$");
        Matcher m = p.matcher(data);
        m.matches();
        Channel chan = (Channel) con.getChannel(m.group(3).toLowerCase());
        if (topicMap.containsKey(chan)) {
            TopicEventImpl tEvent = (TopicEventImpl) topicMap.get(chan);
            topicMap.remove(chan);
            tEvent.setSetBy(m.group(4));
            tEvent.setSetWhen(m.group(5));
            chan.setTopicEvent(tEvent);
            manager.addToRelayList(tEvent);
        }
    }
