    private int getChannelDmxLevel(final long now, final int channelIndex) {
        float max = 0;
        for (int i = 0; i < context.getNumberOfChannelLevelProviders(); i++) {
            float level = context.getChannelLevelProvider(i).getValue(now, channelIndex);
            if (level > max) {
                max = level;
            }
        }
        return Dmx.getDmxValue(max);
    }
