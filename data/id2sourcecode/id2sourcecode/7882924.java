    @Override
    protected ICircle construct(int x1, int y1, int x2, int y2) {
        int r1 = Math.abs(y2 - y1);
        int r2 = Math.abs(x2 - x1);
        if (r2 < r1) {
            r1 = r2;
        }
        int ox = (x1 + x2) / 2;
        int oy = (y1 + y2) / 2;
        return new TwoDCircle(new TwoDPoint(ox, oy), r1);
    }
