    public boolean equals(Object o) {
        if (!(o instanceof ChannelMapOperation)) {
            return false;
        }
        ChannelMapOperation c = (ChannelMapOperation) o;
        if (channelPoints.size() != c.channelPoints.size()) {
            return false;
        }
        String[] channelNames = getChannelNames();
        for (int n = 0; n < channelNames.length; n++) {
            Point2D[] p1 = (Point2D[]) channelPoints.get(channelNames[n]);
            Point2D[] p2 = (Point2D[]) c.channelPoints.get(channelNames[n]);
            if (p2 == null || p2.length != p1.length) {
                return false;
            }
            for (int i = 0; i < p1.length; i++) {
                if (!p1[i].equals(p2[i])) {
                    return false;
                }
            }
        }
        return true;
    }
