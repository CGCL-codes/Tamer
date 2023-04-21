    public void onTopicChanged(TopicEvent topicEvent) {
        System.out.println("onTopicChanged");
        assertEquals("onTopicChanged(): userInfo.getNick()", "Scurvy", topicEvent.getUser().getNick());
        assertEquals("onTopicChanged(): userInfo.getUser()", "~Scurvy", topicEvent.getUser().getUser());
        assertEquals("onTopicChanged(): userInfo.getHost()", "pcp825822pcs.nrockv01.md.comcast.net", topicEvent.getUser().getHostName());
        assertEquals("onTopicChanged(): channel", "#sharktest", topicEvent.getChannel());
        assertEquals("onTopicChanged(): new topic", "A new topic", topicEvent.getNewTopic());
    }
