    private void copyChannelLevels(final LightCueDetail oldDetail, final LightCueDetail newDetail) {
        int channelCount = Math.min(oldShow.getNumberOfChannels(), newShow.getNumberOfChannels());
        for (int channelIndex = 0; channelIndex < channelCount; channelIndex++) {
            CueChannelLevel oldLevel = oldDetail.getChannelLevel(channelIndex);
            CueChannelLevel newLevel = newDetail.getChannelLevel(channelIndex);
            newLevel.setDerived(oldLevel.isDerived());
            LevelValue oldChannelLevel = oldLevel.getChannelLevelValue();
            LevelValue newChannelLevel = newLevel.getChannelLevelValue();
            newChannelLevel.setActive(oldChannelLevel.isActive());
            newChannelLevel.setValue(oldChannelLevel.getValue());
            LevelValue oldSubmasterLevel = oldLevel.getSubmasterLevelValue();
            LevelValue newSubmasterLevel = newLevel.getSubmasterLevelValue();
            newSubmasterLevel.setActive(oldSubmasterLevel.isActive());
            newSubmasterLevel.setValue(oldSubmasterLevel.getValue());
        }
    }
