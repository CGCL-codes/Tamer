    private static TwoDNode generate(int d, IPoint points[], int left, int right) {
        if (right < left) {
            return null;
        }
        if (right == left) {
            return construct(d, points[left]);
        }
        int m = 1 + (right - left) / 2;
        Selection.select(points, m, left, right, comparators[d]);
        TwoDNode dm = construct(d, points[left + m - 1]);
        if (++d > 2) {
            d = 1;
        }
        dm.setBelow(generate(d, points, left, left + m - 2));
        dm.setAbove(generate(d, points, left + m, right));
        return dm;
    }
