    private void computeIntersectsForChain(int start0, int end0, MonotoneChainEdge mce, int start1, int end1, SegmentIntersector ei) {
        Coordinate p00 = pts[start0];
        Coordinate p01 = pts[end0];
        Coordinate p10 = mce.pts[start1];
        Coordinate p11 = mce.pts[end1];
        if (end0 - start0 == 1 && end1 - start1 == 1) {
            ei.addIntersections(e, start0, mce.e, start1);
            return;
        }
        env1.init(p00, p01);
        env2.init(p10, p11);
        if (!env1.intersects(env2)) return;
        int mid0 = (start0 + end0) / 2;
        int mid1 = (start1 + end1) / 2;
        if (start0 < mid0) {
            if (start1 < mid1) computeIntersectsForChain(start0, mid0, mce, start1, mid1, ei);
            if (mid1 < end1) computeIntersectsForChain(start0, mid0, mce, mid1, end1, ei);
        }
        if (mid0 < end0) {
            if (start1 < mid1) computeIntersectsForChain(mid0, end0, mce, start1, mid1, ei);
            if (mid1 < end1) computeIntersectsForChain(mid0, end0, mce, mid1, end1, ei);
        }
    }
