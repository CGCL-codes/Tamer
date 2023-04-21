    public void addDefaultChannel(String id) {
        if (defaultChannels == null) defaultChannels = new ArrayList<String>(); else if (defaultChannels.contains(id)) return;
        if (isStarted()) {
            List<String> channelIds = getMessageBroker().getChannelIds();
            if (channelIds == null || !channelIds.contains(id)) {
                if (Log.isWarn()) {
                    Log.getLogger(getLogCategory()).warn("No channel with id '{0}' is known by the MessageBroker." + " Not adding the channel.", new Object[] { id });
                }
                return;
            }
        }
        defaultChannels.add(id);
    }
