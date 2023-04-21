    private void defaultChannelLevels(final LightCueDetail oldDetail, final LightCueDetail newDetail) {
        int oldChannelCount = oldShow.getNumberOfChannels();
        int newChannelCount = newShow.getNumberOfChannels();
        for (int channelIndex = oldChannelCount; channelIndex < newChannelCount; channelIndex++) {
            CueChannelLevel newLevel = newDetail.getChannelLevel(channelIndex);
            newLevel.setDerived(true);
            LevelValue newChannelLevel = newLevel.getChannelLevelValue();
            newChannelLevel.setValue(0);
            newChannelLevel.setActive(false);
            LevelValue newSubmasterLevel = newLevel.getSubmasterLevelValue();
            newSubmasterLevel.setValue(0);
            newSubmasterLevel.setActive(false);
        }
    }
