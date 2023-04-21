    public void addChannel(String id) {
        if (channelIds == null) channelIds = new ArrayList<String>(); else if (channelIds.contains(id)) return;
        if (isStarted()) {
            List<String> brokerChannelIds = getService().getMessageBroker().getChannelIds();
            if (brokerChannelIds == null || !brokerChannelIds.contains(id)) {
                if (Log.isWarn()) {
                    Log.getLogger(getLogCategory()).warn("No channel with id '{0}' is known by the MessageBroker." + " Not adding the channel.", new Object[] { id });
                }
                return;
            }
        }
        channelIds.add(id);
    }
