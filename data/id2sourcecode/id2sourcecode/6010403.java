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
        BlockViewContainerPart vcp1, vcp2;
        Rectangle r1, r2;
        while ((lo <= end_lo) && (start_hi <= hi)) {
            vcp1 = (BlockViewContainerPart) toSortItems.get(lo);
            vcp2 = (BlockViewContainerPart) toSortItems.get(start_hi);
            r1 = vcp1.getBounds();
            r2 = vcp2.getBounds();
            if (r1.y < r2.y || (r1.y == r2.y && r1.x < r2.x)) {
                lo++;
            } else {
                vcp1 = (BlockViewContainerPart) toSortItems.get(start_hi);
                for (int k = start_hi - 1; k >= lo; k--) {
                    toSortItems.set(k + 1, toSortItems.get(k));
                }
                toSortItems.set(lo, vcp1);
                lo++;
                end_lo++;
                start_hi++;
            }
        }
    }
