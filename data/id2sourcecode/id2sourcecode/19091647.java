    private void setSubmasterActive(final int cueIndex, final int channelIndex, final boolean active) {
        lightCues.getDetail(cueIndex).getChannelLevel(channelIndex).getSubmasterLevelValue().setActive(active);
    }
