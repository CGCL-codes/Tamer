    public void printChannelLevels(final LightCueDetail detail) {
        for (int channelIndex = 0; channelIndex < detail.getNumberOfChannels(); channelIndex++) {
            CueChannelLevel level = detail.getChannelLevel(channelIndex);
            printf(" %d@%s", channelIndex, level.isActive() ? Integer.toString(level.getIntValue()) : "-");
        }
    }
