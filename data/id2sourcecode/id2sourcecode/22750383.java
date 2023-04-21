    public void setChannels(List<String> ids) {
        if (ids != null && isStarted()) {
            List<String> brokerChannelIds = getService().getMessageBroker().getChannelIds();
            for (Iterator<String> iter = ids.iterator(); iter.hasNext(); ) {
                String id = iter.next();
                if (brokerChannelIds == null || !brokerChannelIds.contains(id)) {
                    iter.remove();
                    if (Log.isWarn()) {
                        Log.getLogger(getLogCategory()).warn("No channel with id '{0}' is known by the MessageBroker." + " Not adding the channel.", new Object[] { id });
                    }
                }
            }
        }
        channelIds = ids;
    }
