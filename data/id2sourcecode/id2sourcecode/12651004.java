    private void updateSubsequentChannels(final int lightCueIndex, final int channelIndex, final float value) {
        boolean foundNonDerived = false;
        for (int i = lightCueIndex + 1; !foundNonDerived && i < size(); i++) {
            CueChannelLevel next = getDetail(i).getChannelLevel(channelIndex);
            if (next.isDerived() && next.isActive()) {
                next.setChannelValue(value);
                fireChannelLevelChanged(i, channelIndex);
            } else {
                foundNonDerived = true;
            }
        }
    }
