    public Object getValueAt(final int row, final int column) {
        switch(column) {
            case SOURCE_COLUMN:
                return MODEL.getChannelSourceID(row);
            case CHANNEL_COLUMN:
                return MODEL.getChannelID(row);
            case ENABLE_COLUMN:
                return MODEL.isChannelEnabled(row);
            case PLOTTING_COLUMN:
                return MODEL.isChannelPlotting(row);
            default:
                return null;
        }
    }
