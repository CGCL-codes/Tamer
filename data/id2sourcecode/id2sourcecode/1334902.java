    public TemporaryQueue createTemporaryQueue() throws JMSException {
        try {
            String tempName;
            synchronized (guard) {
                tempName = getName() + ":temp" + tempCount;
                tempCount++;
            }
            SomniTemporaryQueue queue = new SomniTemporaryQueue(tempName, ChannelFactoryCache.IT.getChannelFactoryForContext(getContext()), getContext());
            SomniQueueCache.IT.putTemporaryQueue(queue);
            return queue;
        } catch (NamingException ne) {
            throw new SomniNamingException(ne);
        }
    }
