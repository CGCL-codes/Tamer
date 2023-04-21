    public void onTopicRequest(TopicRequestEvent topicRequestEvent) {
        System.out.println("onTopicRequest");
        assertEquals("onTopicRequest(): channel", "#sharktest", topicRequestEvent.getChannel());
        assertEquals("onTopicRequest(): topic", "A new topic", topicRequestEvent.getTopic());
    }
