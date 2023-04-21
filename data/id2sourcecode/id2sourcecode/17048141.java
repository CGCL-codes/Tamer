    private void qSort(int[] a, int i, int j) {
        if (i >= j - 1) return;
        int rand = i + r.nextInt(j - i);
        if (rand < j - 1) {
            a[rand] += a[j - 1];
            a[j - 1] = a[rand] - a[j - 1];
            a[rand] -= a[j - 1];
        }
        int p = i;
        for (int k = i; k < j - 1; k++) {
            if (a[k] <= a[j - 1]) {
                if (p < k) {
                    a[p] = a[p] + a[k];
                    a[k] = a[p] - a[k];
                    a[p] = a[p] - a[k];
                }
                p++;
            }
        }
        if (p < j - 1) {
            a[p] += a[j - 1];
            a[j - 1] = a[p] - a[j - 1];
            a[p] -= a[j - 1];
        }
        qSort(a, i, p);
        qSort(a, p + 1, j);
    }
