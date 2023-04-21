    private static final void IntroSortLoop(long a[], int l, int r, Object b[], int maxdepth) {
        while ((r - l) > M) {
            if (maxdepth <= 0) {
                HeapSort.sort(a, l, r, b);
                return;
            }
            maxdepth--;
            int i = (l + r) / 2;
            int j;
            long partionElement;
            if (a[l] > a[i]) {
                SwapVals.swap(a, l, i);
                if (b != null) SwapVals.swap(b, l, i);
            }
            if (a[l] > a[r]) {
                SwapVals.swap(a, l, r);
                if (b != null) SwapVals.swap(b, l, r);
            }
            if (a[i] > a[r]) {
                SwapVals.swap(a, i, r);
                if (b != null) SwapVals.swap(b, i, r);
            }
            partionElement = a[i];
            i = l + 1;
            j = r - 1;
            while (i <= j) {
                while ((i < r) && (partionElement > a[i])) ++i;
                while ((j > l) && (partionElement < a[j])) --j;
                if (i <= j) {
                    SwapVals.swap(a, i, j);
                    if (b != null) SwapVals.swap(b, i, j);
                    ++i;
                    --j;
                }
            }
            if (l < j) IntroSortLoop(a, l, j, b, maxdepth);
            if (i >= r) break;
            l = i;
        }
    }
