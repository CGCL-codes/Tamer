    static TopicEvent topic(String data, Connection con) {
        Matcher m = topicPattern.matcher(data);
        if (m.matches()) {
            TopicEvent topicEvent = new TopicEventImpl(data, myManager.getSessionFor(con), con.getChannel(m.group(3).toLowerCase()), m.group(4));
            return topicEvent;
        }
        debug("TOPIC", data);
        return null;
    }
