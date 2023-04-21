    public void setEndPoints(long x1, long y1, long x2, long y2) {
        vx = (x1 + x2) / 2;
        vy = (y1 + y2) / 2;
        vw = (x2 - x1) / 2;
        vh = (y2 - y1) / 2;
        computeSize();
        try {
            vsm.repaintNow();
        } catch (NullPointerException e) {
        }
    }
