    private static int findLine(int pos, int[] indexes, int lo, int tooHi) {
        assert (lo < tooHi);
        if (lo == tooHi - 1) {
            return lo;
        }
        int mid = lo + (tooHi - lo) / 2;
        assert (lo < mid);
        if (pos < indexes[mid]) {
            return findLine(pos, indexes, lo, mid);
        } else {
            return findLine(pos, indexes, mid, tooHi);
        }
    }
