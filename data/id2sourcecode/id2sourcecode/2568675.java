    private void printCueChannelLevel(final CueChannelLevel cueChannelLevel, final int channelIndex) {
        LevelValue channelLevelValue = cueChannelLevel.getChannelLevelValue();
        LevelValue submasterLevelValue = cueChannelLevel.getSubmasterLevelValue();
        printf(" %d@%s..%s%s", channelIndex, formatted(channelLevelValue), formatted(submasterLevelValue), derived(cueChannelLevel));
    }
