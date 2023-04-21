    public void resetChannel(final int lightCueIndex, final int channelIndex) {
        float value = 0f;
        if (lightCueIndex > 0) {
            LightCueDetail detail = getDetail(lightCueIndex - 1);
            value = detail.getChannelLevel(channelIndex).getChannelLevelValue().getValue();
        }
        updateChannel(lightCueIndex, channelIndex, true, value);
        markDirty();
    }
