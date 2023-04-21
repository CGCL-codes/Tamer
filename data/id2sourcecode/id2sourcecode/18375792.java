    public List<InputChannelItemInterface> getChannels() {
        List<InputChannelItemInterface> channelList = new ArrayList<InputChannelItemInterface>(channelBindings.size());
        for (Iterator<StripChannelBinding> it = channelBindings.iterator(); it.hasNext(); ) {
            final StripChannelBinding b = it.next();
            if (b.getBoundChannel() != null) {
                channelList.add(b.getBoundChannel());
            }
        }
        return channelList;
    }
