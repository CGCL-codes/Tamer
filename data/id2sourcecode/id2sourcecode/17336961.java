    public ColorCurve getChannelCurve(String channel) {
        ColorCurve c = null;
        if (channelPoints.containsKey(channel)) {
            c = new ColorCurve();
            Point2D[] points = (Point2D[]) channelPoints.get(channel);
            for (int n = 0; n < points.length; n++) {
                Point2D p = points[n];
                c.addPoint(p.getX(), p.getY());
            }
        }
        return c;
    }
