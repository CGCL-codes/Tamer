    private static void dualPivotQuicksort(final int[] a, final int low, final int high) {
        if (high - low < 31) {
            insertionsort0(a, low, high + 1);
            return;
        }
        final int seventh = (high - low + 1 >>> 3) + (high - low + 1 >>> 6) + 1;
        final int e3 = low + high >>> 1;
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
        final int pivot1, pivot2;
        int left = low;
        int right = high;
        if ((pivot1 = a[e2]) != (pivot2 = a[e4])) {
            a[e2] = a[low];
            a[e4] = a[high];
            while (a[++left] < pivot1) {
            }
            while (a[--right] > pivot2) {
            }
            outer: for (int k = left; k <= right; k++) {
                final int ak = a[k];
                if (ak < pivot1) {
                    a[k] = a[left];
                    a[left] = ak;
                    left++;
                } else if (ak > pivot2) {
                    while (a[right] > pivot2) {
                        if (right-- == k) break outer;
                    }
                    if (a[right] < pivot1) {
                        a[k] = a[left];
                        a[left] = a[right];
                        left++;
                    } else {
                        a[k] = a[right];
                    }
                    a[right] = ak;
                    right--;
                }
            }
            a[low] = a[left - 1];
            a[left - 1] = pivot1;
            a[high] = a[right + 1];
            a[right + 1] = pivot2;
            dualPivotQuicksort(a, low, left - 2);
            dualPivotQuicksort(a, right + 2, high);
            if (left < e1 && e5 < right) {
                while (a[left] == pivot1) left++;
                while (a[right] == pivot2) right--;
                outer: for (int k = left; k <= right; k++) {
                    final int ak = a[k];
                    if (ak == pivot1) {
                        a[k] = a[left];
                        a[left] = ak;
                        left++;
                    } else if (ak == pivot2) {
                        while (a[right] == pivot2) {
                            if (right-- == k) break outer;
                        }
                        if (a[right] == pivot1) {
                            a[k] = a[left];
                            a[left] = pivot1;
                            left++;
                        } else {
                            a[k] = a[right];
                        }
                        a[right] = ak;
                        right--;
                    }
                }
            }
            dualPivotQuicksort(a, left, right);
        } else {
            for (int k = low; k <= right; k++) {
                if (a[k] == pivot1) continue;
                final int ak = a[k];
                if (ak < pivot1) {
                    a[k] = a[left];
                    a[left] = ak;
                    left++;
                } else {
                    while (a[right] > pivot1) {
                        right--;
                    }
                    if (a[right] < pivot1) {
                        a[k] = a[left];
                        a[left] = a[right];
                        left++;
                    } else {
                        a[k] = pivot1;
                    }
                    a[right] = ak;
                    right--;
                }
            }
            dualPivotQuicksort(a, low, left - 1);
            dualPivotQuicksort(a, right + 1, high);
        }
    }
