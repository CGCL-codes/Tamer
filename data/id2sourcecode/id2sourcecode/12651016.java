    private void updateCueChannelSubmasterActiveIndicators(final int lightCueIndex) {
        LightCueDetail detail = getDetail(lightCueIndex);
        for (int channelIndex = 0; channelIndex < detail.getNumberOfChannels(); channelIndex++) {
            boolean active = isChannelActiveInOneOfTheSubmasters(detail, channelIndex);
            CueChannelLevel channelLevel = detail.getChannelLevel(channelIndex);
            LevelValue levelValue = channelLevel.getSubmasterLevelValue();
            if (levelValue.isActive() != active) {
                levelValue.setActive(active);
                fireChannelLevelChanged(lightCueIndex, channelIndex);
            }
        }
    }
