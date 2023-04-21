    public void importSettings(StripChannel channel) {
        if (channel == null) {
            return;
        }
        setChannelID(channel.getChannelID());
        setLabel(channel.getLabel());
        setType(channel.getType());
        if (parentStrip != null) parentStrip.getStripHandler().rebuildChannelStripMap();
    }
