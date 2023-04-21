    private void sort(boolean[] a, int lo0, int hi0, boolean up) {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        sort(a, lo, mid, up);
        sort(a, mid + 1, hi, up);
        int end_lo = mid;
        int start_hi = mid + 1;
        while ((lo <= end_lo) && (start_hi <= hi)) {
            if (up) {
                if (compareBoolean(a[fSortOrder[lo]], a[fSortOrder[start_hi]]) <= 0) {
                    lo++;
                } else {
                    int T = fSortOrder[start_hi];
                    for (int k = start_hi - 1; k >= lo; k--) {
                        fSortOrder[k + 1] = fSortOrder[k];
                    }
                    fSortOrder[lo] = T;
                    lo++;
                    end_lo++;
                    start_hi++;
                }
            } else {
                if (compareBoolean(a[fSortOrder[lo]], a[fSortOrder[start_hi]]) >= 0) {
                    lo++;
                } else {
                    int T = fSortOrder[start_hi];
                    for (int k = start_hi - 1; k >= lo; k--) {
                        fSortOrder[k + 1] = fSortOrder[k];
                    }
                    fSortOrder[lo] = T;
                    lo++;
                    end_lo++;
                    start_hi++;
                }
            }
        }
    }
