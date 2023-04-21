        protected void fire(Object obj) {
            Topic topic = (Topic) obj;
            TopicEvent event = new TopicEvent(owner, topic);
            owner.fireTopicReceived(event);
            topic.getChannel().fireTopicReceived(event);
        }
