    public void activateMemoryChannels4CG(ChannelGuide cg) {
        Iterator it = cg.getChannels().iterator();
        while (it.hasNext()) {
            ChannelGuideEntry entry = (ChannelGuideEntry) it.next();
            ChannelIF channel = (ChannelIF) entry.rssBackEndhandle();
            int chan_upd_ms = GlobalModel.SINGLETON.isDebugMode() ? DBG_MEMCHAN_UPDATE_MS : DEFAULT_MEMCHAN_UPDATE_MS;
            if (channel instanceof de.nava.informa.impl.basic.Channel) channelRegistry.activateChannel(channel, chan_upd_ms);
        }
    }
