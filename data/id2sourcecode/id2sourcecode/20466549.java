    protected void internalMergeSort(List toSortItems, int lo0, int hi0) {
        int lo = lo0;
        int hi = hi0;
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        internalMergeSort(toSortItems, lo, mid);
        internalMergeSort(toSortItems, mid + 1, hi);
        int end_lo = mid;
        int start_hi = mid + 1;
        RDFNode item1, item2;
        while ((lo <= end_lo) && (start_hi <= hi)) {
            item1 = (RDFNode) toSortItems.get(lo);
            item2 = (RDFNode) toSortItems.get(start_hi);
            if (compareItems(item1, item2) < 0) {
                lo++;
            } else {
                item1 = (RDFNode) toSortItems.get(start_hi);
                for (int k = start_hi - 1; k >= lo; k--) {
                    toSortItems.set(k + 1, toSortItems.get(k));
                }
                toSortItems.set(lo, item1);
                lo++;
                end_lo++;
                start_hi++;
            }
        }
    }
