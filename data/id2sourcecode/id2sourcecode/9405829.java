    SomniQueue getQueue(String key, Context context) throws SomniNamingException {
        try {
            synchronized (guard) {
                if (containsQueue(key)) {
                    return (SomniQueue) keysToQueues.get(key);
                } else {
                    ChannelFactory factory = ChannelFactoryCache.IT.getChannelFactoryForContext(context);
                    SomniQueue queue = new SomniQueue(key, factory, context);
                    keysToQueues.put(key, queue);
                    SomniLogger.IT.config("Added " + queue.getName() + " Queue.");
                    return queue;
                }
            }
        } catch (NamingException ne) {
            throw new SomniNamingException(ne);
        }
    }
