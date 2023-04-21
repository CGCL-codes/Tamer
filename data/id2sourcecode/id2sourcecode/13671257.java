    private int getNearestIndexPriv(int orderingValue, int from, int to) {
        if (from == to) return from;
        int center = (from + to) / 2;
        int val = listOrdering.get(center);
        if (val == orderingValue) return center;
        if (val < orderingValue) {
            if (from + 1 == to) {
                return to;
            }
            return getNearestIndexPriv(orderingValue, center, to);
        }
        if (from + 1 == to) {
            return from;
        }
        return getNearestIndexPriv(orderingValue, from, center);
    }
