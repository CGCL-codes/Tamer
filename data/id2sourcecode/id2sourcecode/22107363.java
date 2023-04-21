    private void qsort(Object[] items, Comparator comparator, int l, int r) {
        final int M = 4;
        int i;
        int j;
        Object v;
        if ((r - l) > M) {
            i = (r + l) / 2;
            if (comparator.compare(items[l], items[i]) > 0) {
                swap(items, l, i);
            }
            if (comparator.compare(items[l], items[r]) > 0) {
                swap(items, l, r);
            }
            if (comparator.compare(items[i], items[r]) > 0) {
                swap(items, i, r);
            }
            j = r - 1;
            swap(items, i, j);
            i = l;
            v = items[j];
            while (true) {
                while (comparator.compare(items[++i], v) < 0) {
                }
                while (comparator.compare(items[--j], v) > 0) {
                }
                if (j < i) {
                    break;
                }
                swap(items, i, j);
            }
            swap(items, i, r - 1);
            qsort(items, comparator, l, j);
            qsort(items, comparator, i + 1, r);
        }
    }
