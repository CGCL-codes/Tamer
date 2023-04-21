    private static final <C> void IntroSortLoop(C a[], Comparator<C> comp, int l, int r, int maxdepth) {
        while ((r - l) > M) {
            if (maxdepth <= 0) {
                HeapSort.sort(a, comp, l, r);
                return;
            }
            maxdepth--;
            int i = (l + r) / 2;
            int j;
            C partionElement;
            if (comp.compare(a[l], a[i]) > 0) SwapVals.swap(a, l, i);
            if (comp.compare(a[l], a[r]) > 0) SwapVals.swap(a, l, r);
            if (comp.compare(a[i], a[r]) > 0) SwapVals.swap(a, i, r);
            partionElement = a[i];
            i = l + 1;
            j = r - 1;
            while (i <= j) {
                while ((i < r) && (comp.compare(partionElement, a[i]) > 0)) ++i;
                while ((j > l) && (comp.compare(partionElement, a[j]) < 0)) --j;
                if (i <= j) {
                    SwapVals.swap(a, i, j);
                    ++i;
                    --j;
                }
            }
            if (l < j) IntroSortLoop(a, comp, l, j, maxdepth);
            if (i >= r) break;
            l = i;
        }
    }
