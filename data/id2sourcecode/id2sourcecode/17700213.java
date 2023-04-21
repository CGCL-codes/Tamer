    private static final <C extends Comparable<? super C>> void IntroSortLoop(List<C> a, int l, int r, int maxdepth) {
        while ((r - l) > M) {
            if (maxdepth <= 0) {
                HeapSort.sort(a, l, r);
                return;
            }
            maxdepth--;
            int i = (l + r) / 2;
            int j;
            C partionElement;
            if ((a.get(l)).compareTo(a.get(i)) > 0) SwapVals.swap(a, l, i);
            if ((a.get(l)).compareTo(a.get(r)) > 0) SwapVals.swap(a, l, r);
            if ((a.get(i)).compareTo(a.get(r)) > 0) SwapVals.swap(a, i, r);
            partionElement = a.get(i);
            i = l + 1;
            j = r - 1;
            while (i <= j) {
                while ((i < r) && (partionElement.compareTo(a.get(i)) > 0)) ++i;
                while ((j > l) && (partionElement.compareTo(a.get(j)) < 0)) --j;
                if (i <= j) {
                    SwapVals.swap(a, i, j);
                    ++i;
                    --j;
                }
            }
            if (l < j) IntroSortLoop(a, l, j, maxdepth);
            if (i >= r) break;
            l = i;
        }
    }
