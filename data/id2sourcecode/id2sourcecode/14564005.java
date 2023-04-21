    public TemporaryTopic createTemporaryTopic() throws JMSException {
        try {
            String tempName;
            synchronized (guard) {
                tempName = getName() + ":temp" + tempCount;
                tempCount++;
            }
            SomniTemporaryTopic topic = new SomniTemporaryTopic(tempName, ChannelFactoryCache.IT.getChannelFactoryForContext(getContext()), getContext());
            SomniTopicCache.IT.putTemporaryTopic(topic);
            return topic;
        } catch (NamingException ne) {
            throw new SomniNamingException(ne);
        }
    }
