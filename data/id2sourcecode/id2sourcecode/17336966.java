    public int hashCode() {
        int hash = 0;
        String[] channelNames = getChannelNames();
        for (int n = 0; n < channelNames.length; n++) {
            hash = hash * 31 + channelNames[n].hashCode();
            Point2D[] p = (Point2D[]) channelPoints.get(channelNames[n]);
            hash = hash * 31 + p.hashCode();
        }
        return hash;
    }
