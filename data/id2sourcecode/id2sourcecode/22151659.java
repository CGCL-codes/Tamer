    private void updateChannel(final LevelControl levelControl, final int index) {
        if (index < currentChannelIndexes.length) {
            int channelIndex = currentChannelIndexes[index];
            int old = getCues().getLightCues().getDetail(currentCueIndex).getChannelLevel(channelIndex).getChannelIntValue();
            if (old != levelControl.getLevel()) {
                float value = levelControl.getLevel() / 100f;
                getCues().getLightCues().setChannel(currentCueIndex, channelIndex, value);
            }
        }
    }
