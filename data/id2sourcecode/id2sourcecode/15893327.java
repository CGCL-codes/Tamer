    private void computeIntersectsForChain(int start0, int end0, MonotoneChainEdge mce, int start1, int end1, SegmentIntersector ei) {
        if (!(mce instanceof SnapMonotoneChainEdge)) throw new IllegalArgumentException("Requiere MCE de snap");
        SnapMonotoneChainEdge snapMce = (SnapMonotoneChainEdge) mce;
        Coordinate p00 = super.getCoordinates()[start0];
        Coordinate p01 = super.getCoordinates()[end0];
        Coordinate p10 = mce.getCoordinates()[start1];
        Coordinate p11 = mce.getCoordinates()[end1];
        if (end0 - start0 == 1 && end1 - start1 == 1) {
            ei.addIntersections(e, start0, snapMce.getEdge(), start1);
            return;
        }
        Envelope env1 = new Envelope(p00, p01);
        double newMinX = env1.getMinX() - snapTolerance;
        double newMaxX = env1.getMaxX() + snapTolerance;
        double newMinY = env1.getMinY() - snapTolerance;
        double newMaxY = env1.getMaxY() + snapTolerance;
        env1 = new Envelope(newMinX, newMaxX, newMinY, newMaxY);
        Envelope env2 = new Envelope(p10, p11);
        newMinX = env1.getMinX() - snapTolerance;
        newMaxX = env1.getMaxX() + snapTolerance;
        newMinY = env1.getMinY() - snapTolerance;
        newMaxY = env1.getMaxY() + snapTolerance;
        env2 = new Envelope(newMinX, newMaxX, newMinY, newMaxY);
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
