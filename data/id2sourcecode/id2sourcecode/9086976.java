    public static PairResult findClosestPair(double[][] points, int l, int r) {
        if (l + 2 < r) {
            int mid = (r + l) / 2;
            PairResult pr1 = ClosestPair.findClosestPair(points, l, mid);
            PairResult pr2 = ClosestPair.findClosestPair(points, mid + 1, r);
            PairResult pr;
            if (pr1.getDistance() < pr2.getDistance()) pr = pr1; else pr = pr2;
            pr = ClosestPair.combine(points, l, mid, r, pr);
            return pr;
        } else return ClosestPair.findClosestPairSimple(points, l, r);
    }
