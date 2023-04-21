    private Object getValueChannelAt(final int channelIndex, final int col) {
        Object value = "?";
        Channel channel = getShow().getChannels().get(channelIndex);
        switch(col) {
            case COLUMN_ID:
                value = buildLabel(channel);
                break;
            case COLUMN_NAME:
                value = channel.getName();
                break;
            case COLUMN_STAGE_BAR:
                Level level = getChannelLevel(channelIndex);
                value = new LevelBar(level);
                break;
            case COLUMN_STAGE:
                value = getChannelLevel(channelIndex);
                break;
            case COLUMN_SUBMASTER:
                value = emptyCell;
                if (selectedSubmasterIndex != -1) {
                    value = getSelectedSubmaster().getLevel(channelIndex);
                }
                break;
            default:
        }
        return value;
    }
