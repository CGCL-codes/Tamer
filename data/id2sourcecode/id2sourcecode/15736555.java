    public void refineSubMesh(List<Integer> triangleStrip, int xi, int yi, int xj, int yj, int xk, int yk, int level) {
        int xc = (xj + xk) / 2;
        int yc = (yj + yk) / 2;
        boolean refine = level > 1 && isEnabled(xc, yc);
        if (refine) refineSubMesh(triangleStrip, xc, yc, xj, yj, xi, yi, level - 1);
        appendIndex(triangleStrip, xi, yi, level & 1);
        if (refine) refineSubMesh(triangleStrip, xc, yc, xi, yi, xk, yk, level - 1);
    }
