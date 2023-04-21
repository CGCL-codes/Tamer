    private static DimensionalNode generate(int d, int maxD, IMultiPoint points[], int left, int right) {
        if (right < left) {
            return null;
        }
        if (right == left) {
            return new OneHelperKDNode(d, points[left]);
        }
        int m = 1 + (right - left) / 2;
        Selection.select(points, m, left, right, comparators[d]);
        OneHelperKDNode dm = new OneHelperKDNode(d, points[left + m - 1]);
        if (++d > maxD) {
            d = 1;
        }
        dm.setBelow(generate(d, maxD, points, left, left + m - 2));
        dm.setAbove(generate(d, maxD, points, left + m, right));
        return dm;
    }
