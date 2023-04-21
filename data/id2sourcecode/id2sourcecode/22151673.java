    private void doSetFocusChannel(final int cueIndex, final int channelIndex, final int[] channelIndexes) {
        if (focus != FOCUS_CHANNEL) {
            deassign();
        }
        focus = FOCUS_CHANNEL;
        int newRange = channelIndex / getGroupSize();
        if (currentRange != newRange || currentCueIndex != cueIndex) {
            deassignChannels();
            currentRange = newRange;
            currentCueIndex = cueIndex;
            currentChannelIndexes = channelIndexes;
            int index = currentRange * getGroupSize();
            int end = index + getControl().getLevelControls().size();
            if (end > channelIndexes.length) {
                end = channelIndexes.length;
            }
            LightCueDetail detail = getDetail(currentCueIndex);
            for (LevelControl levelControl : getControl().getLevelControls()) {
                if (index < end) {
                    int channelId = channelIndexes[index];
                    Channel channel = context.getShow().getChannels().get(channelId);
                    channel.setLevelControl(levelControl);
                    CueChannelLevel level = detail.getChannelLevel(channelId);
                    level.setLevelControl(levelControl);
                    levelControl.getHolder().setValue(level.getChannelIntValue(), levelControlListener);
                } else {
                    levelControl.setLevel(0);
                }
                index++;
            }
        }
        tableModelDetails.fireTableRowsUpdated(0, tableModelDetails.getRowCount());
        tableModelHeaders.fireTableDataChanged();
    }
