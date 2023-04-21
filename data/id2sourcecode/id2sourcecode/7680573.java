    private void updateChannelDmxOutputs(final long now) {
        for (int i = 0; i < Dmx.MAX_CHANNELS; i++) {
            channelDmxOutputs[i] = getChannelDmxLevel(now, i);
        }
    }
