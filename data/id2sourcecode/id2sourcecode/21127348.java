    public void setDefaultChannels(List<String> ids) {
        if (ids != null && isStarted()) {
            List<String> channelIds = getMessageBroker().getChannelIds();
            for (Iterator<String> iter = ids.iterator(); iter.hasNext(); ) {
                String id = iter.next();
                if (channelIds == null || !channelIds.contains(id)) {
                    iter.remove();
                    if (Log.isWarn()) {
                        Log.getLogger(getLogCategory()).warn("No channel with id '{0}' is known by the MessageBroker." + " Not adding the channel.", new Object[] { id });
                    }
                }
            }
        }
        defaultChannels = ids;
    }
