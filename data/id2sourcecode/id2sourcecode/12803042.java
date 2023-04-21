    public Coord3d getCenter() {
        float cx = (x1 + x2) / 2;
        float cy = (y1 + y2) / 2;
        float cz = (z1 + z2) / 2;
        return new Coord3d(cx, cy, cz);
    }
