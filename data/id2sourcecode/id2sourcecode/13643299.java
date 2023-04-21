    private void computeOverlaps(int start0, int end0, MonotoneChain mc, int start1, int end1, MonotoneChainOverlapAction mco) {
        Coordinate p00 = pts[start0];
        Coordinate p01 = pts[end0];
        Coordinate p10 = mc.pts[start1];
        Coordinate p11 = mc.pts[end1];
        if (end0 - start0 == 1 && end1 - start1 == 1) {
            mco.overlap(this, start0, mc, start1);
            return;
        }
        mco.tempEnv1.init(p00, p01);
        mco.tempEnv2.init(p10, p11);
        if (!mco.tempEnv1.intersects(mco.tempEnv2)) return;
        int mid0 = (start0 + end0) / 2;
        int mid1 = (start1 + end1) / 2;
        if (start0 < mid0) {
            if (start1 < mid1) computeOverlaps(start0, mid0, mc, start1, mid1, mco);
            if (mid1 < end1) computeOverlaps(start0, mid0, mc, mid1, end1, mco);
        }
        if (mid0 < end0) {
            if (start1 < mid1) computeOverlaps(mid0, end0, mc, start1, mid1, mco);
            if (mid1 < end1) computeOverlaps(mid0, end0, mc, mid1, end1, mco);
        }
    }
