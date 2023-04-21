    private void updateSubsequentCues(final int lightCueIndex) {
        LightCueDetail lightCue = getDetail(lightCueIndex);
        for (int i = 0; i < lightCue.getNumberOfChannels(); i++) {
            float value = lightCue.getChannelLevel(i).getChannelLevelValue().getValue();
            updateSubsequentChannels(lightCueIndex, i, value);
        }
        for (int i = 0; i < getNumberOfSubmasters(); i++) {
            float value = lightCue.getSubmasterLevel(i).getValue();
            updateSubsequentSubmasters(lightCueIndex, i, value);
        }
    }
