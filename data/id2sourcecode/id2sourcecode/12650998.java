    private void updateChannelValue(final int lightCueIndex, final int channelIndex) {
        LightCueDetail detail = getDetail(lightCueIndex);
        float value = calculateChannelSubmasterLevelValue(detail, channelIndex);
        CueChannelLevel level = detail.getChannelLevel(channelIndex);
        if (Math.abs(level.getSubmasterValue() - value) > 0.001f) {
            level.setSubmasterValue(value);
            fireChannelLevelChanged(lightCueIndex, channelIndex);
        }
    }
