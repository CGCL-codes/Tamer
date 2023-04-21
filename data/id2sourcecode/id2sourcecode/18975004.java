    private static void dualPivotQuicksort(final int[] a, final int low, final int high, final boolean leftmost) {
        final int length = high - low + 1;
        if (length < 32) {
            if (!leftmost) {
                for (int j, i = low + 1; i <= high; i++) {
                    final int ai = a[i];
                    for (j = i - 1; ai < a[j]; j--) {
                        a[j + 1] = a[j];
                    }
                    a[j + 1] = ai;
                }
            } else {
                for (int i = low, j = i; i < high; j = ++i) {
                    final int ai = a[i + 1];
                    while (ai < a[j]) {
                        a[j + 1] = a[j];
                        if (j-- == low) {
                            break;
                        }
                    }
                    a[j + 1] = ai;
                }
            }
            return;
        }
        final int seventh = (length >>> 3) + (length >>> 6) + 1;
        final int e3 = (low + high) >>> 1;
        final int e2 = e3 - seventh;
        final int e1 = e2 - seventh;
        final int e4 = e3 + seventh;
        final int e5 = e4 + seventh;
        if (a[e2] < a[e1]) {
            final int t = a[e2];
            a[e2] = a[e1];
            a[e1] = t;
        }
        if (a[e3] < a[e2]) {
            final int t = a[e3];
            a[e3] = a[e2];
            a[e2] = t;
            if (t < a[e1]) {
                a[e2] = a[e1];
                a[e1] = t;
            }
        }
        if (a[e4] < a[e3]) {
            final int t = a[e4];
            a[e4] = a[e3];
            a[e3] = t;
            if (t < a[e2]) {
                a[e3] = a[e2];
                a[e2] = t;
                if (t < a[e1]) {
                    a[e2] = a[e1];
                    a[e1] = t;
                }
            }
        }
        if (a[e5] < a[e4]) {
            final int t = a[e5];
            a[e5] = a[e4];
            a[e4] = t;
            if (t < a[e3]) {
                a[e4] = a[e3];
                a[e3] = t;
                if (t < a[e2]) {
                    a[e3] = a[e2];
                    a[e2] = t;
                    if (t < a[e1]) {
                        a[e2] = a[e1];
                        a[e1] = t;
                    }
                }
            }
        }
        final int pivot1 = a[e2];
        final int pivot2 = a[e4];
        int less = low;
        int great = high;
        if (pivot1 != pivot2) {
            a[e2] = a[low];
            a[e4] = a[high];
            while (a[++less] < pivot1) ;
            while (a[--great] > pivot2) ;
            outer: for (int k = less; k <= great; k++) {
                final int ak = a[k];
                if (ak < pivot1) {
                    a[k] = a[less];
                    a[less] = ak;
                    less++;
                } else if (ak > pivot2) {
                    while (a[great] > pivot2) {
                        if (great-- == k) {
                            break outer;
                        }
                    }
                    if (a[great] < pivot1) {
                        a[k] = a[less];
                        a[less] = a[great];
                        less++;
                    } else {
                        a[k] = a[great];
                    }
                    a[great] = ak;
                    great--;
                }
            }
            a[low] = a[less - 1];
            a[less - 1] = pivot1;
            a[high] = a[great + 1];
            a[great + 1] = pivot2;
            dualPivotQuicksort(a, low, less - 2, leftmost);
            dualPivotQuicksort(a, great + 2, high, false);
            if (less < e1 && e5 < great) {
                while (a[less] == pivot1) {
                    less++;
                }
                while (a[great] == pivot2) {
                    great--;
                }
                outer: for (int k = less; k <= great; k++) {
                    final int ak = a[k];
                    if (ak == pivot1) {
                        a[k] = a[less];
                        a[less] = ak;
                        less++;
                    } else if (ak == pivot2) {
                        while (a[great] == pivot2) {
                            if (great-- == k) {
                                break outer;
                            }
                        }
                        if (a[great] == pivot1) {
                            a[k] = a[less];
                            a[less] = pivot1;
                            less++;
                        } else {
                            a[k] = a[great];
                        }
                        a[great] = ak;
                        great--;
                    }
                }
            }
            dualPivotQuicksort(a, less, great, false);
        } else {
            for (int k = low; k <= great; k++) {
                if (a[k] == pivot1) {
                    continue;
                }
                final int ak = a[k];
                if (ak < pivot1) {
                    a[k] = a[less];
                    a[less] = ak;
                    less++;
                } else {
                    while (a[great] > pivot1) {
                        great--;
                    }
                    if (a[great] < pivot1) {
                        a[k] = a[less];
                        a[less] = a[great];
                        less++;
                    } else {
                        a[k] = pivot1;
                    }
                    a[great] = ak;
                    great--;
                }
            }
            dualPivotQuicksort(a, low, less - 1, leftmost);
            dualPivotQuicksort(a, great + 1, high, false);
        }
    }
