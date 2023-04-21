    private void qsort(int min, int n) {
        if (n <= 1) return;
        int i1 = min;
        int i2 = min + (n - 1) / 2;
        int i3 = min + n - 1;
        int tmp;
        if (cmp(i1, i2) > 0) {
            tmp = i1;
            i1 = i2;
            i2 = tmp;
        }
        if (cmp(i2, i3) > 0) {
            tmp = i2;
            i2 = i3;
            i3 = tmp;
        }
        if (cmp(i1, i2) > 0) {
            tmp = i1;
            i1 = i2;
            i2 = tmp;
        }
        int median = i2;
        tmp = inds[min];
        inds[min] = inds[median];
        inds[median] = tmp;
        median = min;
        int i = min + 1;
        int j = min + n - 1;
        OUTER: while (j > i) {
            while (cmp(i, median) < 0) {
                i++;
                if (i >= j) break OUTER;
            }
            while (cmp(j, median) > 0) {
                j--;
                if (i >= j) break OUTER;
            }
            tmp = inds[i];
            inds[i] = inds[j];
            inds[j] = tmp;
        }
        if (cmp(median, j) > 0) {
            tmp = inds[j];
            inds[j] = inds[median];
            inds[median] = tmp;
        }
        qsort(min, j - min);
        qsort(j, min + n - j);
    }
