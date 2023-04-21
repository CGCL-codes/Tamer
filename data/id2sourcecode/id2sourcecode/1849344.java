    public void setEndPoints(double x1, double y1, double x2, double y2) {
        vx = (x1 + x2) / 2;
        vy = (y1 + y2) / 2;
        vw = (x2 - x1);
        vh = (y2 - y1);
        computeSize();
        VirtualSpaceManager.INSTANCE.repaint();
    }
