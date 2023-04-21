    private void updateChannelValueFromPreviousCue(final int lightCueIndex, final int channelIndex) {
        if (lightCueIndex > 0) {
            LightCueDetail detail = getDetail(lightCueIndex);
            LightCueDetail previousLightCue = getDetail(lightCueIndex - 1);
            if (detail.getChannelLevel(channelIndex).isDerived()) {
                float value = previousLightCue.getChannelLevel(channelIndex).getChannelLevelValue().getValue();
                detail.getChannelLevel(channelIndex).setChannelValue(value);
            }
        }
    }
