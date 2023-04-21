    private double[] getCenterPoint(Envelope envelope) {
        double[] xy = new double[2];
        double oldMinX = envelope.getMinX();
        double oldMinY = envelope.getMinY();
        double oldMaxX = envelope.getMaxX();
        double oldMaxY = envelope.getMaxY();
        double mapX = (oldMinX + oldMaxX) / 2;
        double mapY = (oldMinY + oldMaxY) / 2;
        xy[0] = mapX;
        xy[1] = mapY;
        return xy;
    }
