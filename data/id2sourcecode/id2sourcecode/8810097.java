    protected static int firstSmallerPtIndex(int arg, int lo, int[] pts) {
        int hi = pts.length - 1;
        if ((lo > hi) || (pts[hi] > arg)) return -1; else if (arg >= pts[lo]) return lo; else {
            while (lo < hi) {
                int mid = (lo + hi) / 2;
                assert (lo < hi);
                assert (pts[lo] > arg);
                assert (pts[hi] <= arg);
                if (mid == lo) return hi; else if (pts[mid] > arg) lo = mid; else hi = mid;
            }
            assert (false);
            return -1;
        }
    }
