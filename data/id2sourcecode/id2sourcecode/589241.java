    private void QuickSort(int a[], int l, int r) throws Exception {
        int M = 4;
        int i;
        int j;
        int v;
        if ((r - l) > M) {
            i = (r + l) / 2;
            if (a[l] > a[i]) {
                swap(a, l, i);
            }
            if (a[l] > a[r]) {
                swap(a, l, r);
            }
            if (a[i] > a[r]) {
                swap(a, i, r);
            }
            j = r - 1;
            swap(a, i, j);
            i = l;
            v = a[j];
            for (; ; ) {
                while (a[++i] < v) ;
                while (a[--j] > v) ;
                if (j < i) {
                    break;
                }
                swap(a, i, j);
                pause(i, j);
                if (stopRequested) {
                    return;
                }
            }
            swap(a, i, r - 1);
            pause(i);
            QuickSort(a, l, j);
            QuickSort(a, i + 1, r);
        }
    }
