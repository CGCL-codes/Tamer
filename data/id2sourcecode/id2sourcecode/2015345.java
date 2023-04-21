    private int bSearch(int low, int high, float weight) {
        if (high < low) return -1;
        if (weight <= aligns.get(high).getWeight()) return high;
        if (weight >= aligns.get(low).getWeight()) return low;
        if (low == high - 1) return low;
        int mid = (high + low) / 2;
        if (weight < aligns.get(mid).getWeight()) return bSearch(mid, high, weight); else if (weight > aligns.get(mid).getWeight()) return bSearch(low, mid, weight); else return mid;
    }
