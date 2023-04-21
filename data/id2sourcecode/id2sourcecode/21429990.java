    private static void QuickSort(double a[], double b[], int l, int r) {
        int M = 4;
        int i;
        int j;
        double v;
        if ((r - l) > M) {
            i = (r + l) / 2;
            if (a[l] > a[i]) swap(a, b, l, i);
            if (a[l] > a[r]) swap(a, b, l, r);
            if (a[i] > a[r]) swap(a, b, i, r);
            j = r - 1;
            swap(a, b, i, j);
            i = l;
            v = a[j];
            for (; ; ) {
                while (a[++i] < v) ;
                while (a[--j] > v) ;
                if (j < i) break;
                swap(a, b, i, j);
            }
            swap(a, b, i, r - 1);
            QuickSort(a, b, l, j);
            QuickSort(a, b, i + 1, r);
        }
    }
