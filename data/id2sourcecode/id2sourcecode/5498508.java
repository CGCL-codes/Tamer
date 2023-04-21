    private void setLimits(int i) {
        DataGroup group = getGroup(i);
        for (int j = 0; j < getChannelsSize(i); j++) {
            DataChannel channel = getChannel(i, j);
            if (j == group.getXChannel()) {
                setLimit("MinX", Math.min(getLimit("MinX"), channel.minLimit()));
                setLimit("MaxX", Math.max(getLimit("MaxX"), channel.maxLimit()));
            } else {
                setLimit("MinY", Math.min(getLimit("MinY"), channel.minLimit()));
                setLimit("MaxY", Math.max(getLimit("MaxY"), channel.maxLimit()));
            }
        }
    }
