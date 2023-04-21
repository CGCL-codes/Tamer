    public void addChannel(ChannelEventType type) {
        try {
            Method method = listenerClass.getDeclaredMethod(type.getChannel(), new Class[] { eventClass });
            eventTopicDispatcher.addChannel(type, method);
        } catch (SecurityException e) {
            throw new IllegalStateException("Unable to access " + type.getChannel(), e);
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException("Unable to find " + type.getChannel(), e);
        }
    }
