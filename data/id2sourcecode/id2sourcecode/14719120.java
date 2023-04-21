    SomniTopic getTopic(String key, Context context) throws SomniNamingException {
        try {
            synchronized (guard) {
                if (containsTopic(key)) {
                    return (SomniTopic) keysToTopics.get(key);
                } else {
                    ChannelFactory factory = ChannelFactoryCache.IT.getChannelFactoryForContext(context);
                    SomniTopic topic = new SomniTopic(key, factory, context);
                    keysToTopics.put(key, topic);
                    SomniLogger.IT.config("Added " + topic.getName() + " Topic.");
                    return topic;
                }
            }
        } catch (NamingException ne) {
            throw new SomniNamingException(ne);
        }
    }
