    public void addDefaultChannel(String id) {
        if (defaultChannels == null) defaultChannels = new ArrayList<String>(); else if (defaultChannels.contains(id)) return;
        List<String> channelIds = getChannelIds();
        if (channelIds == null || !channelIds.contains(id)) {
            if (Log.isWarn()) {
                Log.getLogger(LOG_CATEGORY).warn("No channel with id '{0}' is known by the MessageBroker." + " Not adding the channel.", new Object[] { id });
            }
            return;
        }
        defaultChannels.add(id);
    }
