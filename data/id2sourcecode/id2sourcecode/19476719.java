    private static void siftdown(long[] a, Object[] b, int n, int vacant, long missing, Object missingB, int drop) {
        int memo = vacant;
        int child, parent;
        int count, next_peek;
        count = 0;
        next_peek = (drop + 1) / 2;
        child = 2 * (vacant + 1);
        while (child < n) {
            if (a[child] < a[child - 1]) child--;
            a[vacant] = a[child];
            if (b != null) b[vacant] = b[child];
            vacant = child;
            child = 2 * (vacant + 1);
            count++;
            if (count == next_peek) {
                if (a[(vacant - 1) / 2] <= missing) break; else next_peek = (count + drop + 1) / 2;
            }
        }
        if (child == n) {
            a[vacant] = a[n - 1];
            if (b != null) b[vacant] = b[n - 1];
            vacant = n - 1;
        }
        parent = (vacant - 1) / 2;
        while (vacant > memo) {
            if (a[parent] < missing) {
                a[vacant] = a[parent];
                if (b != null) b[vacant] = b[parent];
                vacant = parent;
                parent = (vacant - 1) / 2;
            } else break;
        }
        a[vacant] = missing;
        if (b != null) b[vacant] = missingB;
    }
