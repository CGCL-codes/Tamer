    private void printCueChannelLevels(final LightCueDetail detail) {
        for (int channelIndex = 0; channelIndex < detail.getNumberOfChannels(); channelIndex++) {
            CueChannelLevel level = detail.getChannelLevel(channelIndex);
            printCueChannelLevel(level, channelIndex);
        }
    }
