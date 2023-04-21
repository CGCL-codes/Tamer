    public ChannelChanges getChannelChanges() {
        ChannelChanges changes = new ChannelChanges();
        int channelId = 0;
        for (CueChannelLevel level : channelLevels) {
            channelId++;
            if (level.isActive()) {
                int dmxValue = Dmx.getDmxValue(level.getValue());
                changes.add(channelId, dmxValue);
            }
        }
        return changes;
    }
