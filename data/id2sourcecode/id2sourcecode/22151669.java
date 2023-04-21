    private void deassignChannels() {
        if (currentRange >= 0 && currentCueIndex >= 0) {
            int start = currentRange * getControl().getSheetGroupSize();
            int end = start + getControl().getLevelControls().size();
            if (end > currentChannelIndexes.length) {
                end = currentChannelIndexes.length;
            }
            LightCueDetail detail = getDetail(currentCueIndex);
            for (int i = start; i < end; i++) {
                int channelIndex = currentChannelIndexes[i];
                Channel channel = context.getShow().getChannels().get(channelIndex);
                channel.setLevelControl(null);
                tableModelHeaders.fireTableDataChanged();
                CueChannelLevel level = detail.getChannelLevel(channelIndex);
                level.setLevelControl(null);
            }
        }
        currentRange = -1;
        currentCueIndex = -1;
        currentChannelIndexes = new int[0];
    }
