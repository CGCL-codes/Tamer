    void sort(int a[], int lo, int hi, int scratch[]) throws Exception {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        sort(a, lo, mid, scratch);
        sort(a, mid + 1, hi, scratch);
        int k, t_lo = lo, t_hi = mid + 1;
        for (k = lo; k <= hi; k++) if ((t_lo <= mid) && ((t_hi > hi) || (a[t_lo] < a[t_hi]))) {
            scratch[k] = a[t_lo++];
            pause(t_lo, t_hi);
        } else {
            scratch[k] = a[t_hi++];
            pause(t_lo, t_hi);
        }
        for (k = lo; k <= hi; k++) {
            a[k] = scratch[k];
            pause(k);
        }
    }
