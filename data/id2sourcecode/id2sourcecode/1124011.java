    public void findRootBetween(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
        try {
            xx = (x1 + x2) / 2;
            yy = (y1 + y2) / 2;
            zz = evaluateF(xx, yy);
            if (Math.abs(zz) < 1e-10) {
                return;
            }
            if (zz * z1 < 0) {
                findRootBetween(x1, y1, z1, xx, yy, zz);
            } else {
                findRootBetween(xx, yy, zz, x2, y2, z2);
            }
        } catch (final Exception e) {
        }
    }
