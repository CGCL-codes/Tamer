    public VSegment(long x1, long y1, int z, Color c, long x2, long y2) {
        vx = (x1 + x2) / 2;
        vy = (y1 + y2) / 2;
        vz = z;
        vw = (x2 - x1) / 2;
        vh = (y2 - y1) / 2;
        computeSize();
        setColor(c);
    }
