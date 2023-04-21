    private void computeSelect(Envelope searchEnv, int start0, int end0, MonotoneChainSelectAction mcs) {
        Coordinate p0 = pts[start0];
        Coordinate p1 = pts[end0];
        mcs.tempEnv1.init(p0, p1);
        if (end0 - start0 == 1) {
            mcs.select(this, start0);
            return;
        }
        if (!searchEnv.intersects(mcs.tempEnv1)) return;
        int mid = (start0 + end0) / 2;
        if (start0 < mid) {
            computeSelect(searchEnv, start0, mid, mcs);
        }
        if (mid < end0) {
            computeSelect(searchEnv, mid, end0, mcs);
        }
    }
