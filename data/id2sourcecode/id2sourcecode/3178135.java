    private static <E> void dualPivotQuicksort(final E[] a, final int low, final int high, final Comparator<? super E> cmp) {
        if (high - low < 16) {
            insertionsort0(a, low, high + 1, cmp);
            return;
        }
        final int seventh = (high - low + 1 >>> 3) + (high - low + 1 >>> 6) + 1;
        final int e3 = low + high >>> 1;
        final int e2 = e3 - seventh;
        final int e1 = e2 - seventh;
        final int e4 = e3 + seventh;
        final int e5 = e4 + seventh;
        if (cmp.compare(a[e2], a[e1]) < 0) {
            final E t = a[e2];
            a[e2] = a[e1];
            a[e1] = t;
        }
        if (cmp.compare(a[e3], a[e2]) < 0) {
            final E t = a[e3];
            a[e3] = a[e2];
            a[e2] = t;
            if (cmp.compare(t, a[e1]) < 0) {
                a[e2] = a[e1];
                a[e1] = t;
            }
        }
        if (cmp.compare(a[e4], a[e3]) < 0) {
            final E t = a[e4];
            a[e4] = a[e3];
            a[e3] = t;
            if (cmp.compare(t, a[e2]) < 0) {
                a[e3] = a[e2];
                a[e2] = t;
                if (cmp.compare(t, a[e1]) < 0) {
                    a[e2] = a[e1];
                    a[e1] = t;
                }
            }
        }
        if (cmp.compare(a[e5], a[e4]) < 0) {
            final E t = a[e5];
            a[e5] = a[e4];
            a[e4] = t;
            if (cmp.compare(t, a[e3]) < 0) {
                a[e4] = a[e3];
                a[e3] = t;
                if (cmp.compare(t, a[e2]) < 0) {
                    a[e3] = a[e2];
                    a[e2] = t;
                    if (cmp.compare(t, a[e1]) < 0) {
                        a[e2] = a[e1];
                        a[e1] = t;
                    }
                }
            }
        }
        final E pivot1, pivot2;
        int left = low;
        int right = high;
        if (cmp.compare(pivot1 = a[e2], pivot2 = a[e4]) != 0) {
            a[e2] = a[low];
            a[e4] = a[high];
            while (cmp.compare(a[++left], pivot1) < 0) {
            }
            while (cmp.compare(a[--right], pivot2) > 0) {
            }
            outer: for (int k = left; k <= right; k++) {
                final E ak = a[k];
                if (cmp.compare(ak, pivot1) < 0) {
                    a[k] = a[left];
                    a[left] = ak;
                    left++;
                } else if (cmp.compare(ak, pivot2) > 0) {
                    while (cmp.compare(a[right], pivot2) > 0) {
                        if (right-- == k) break outer;
                    }
                    if (cmp.compare(a[right], pivot1) < 0) {
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
            dualPivotQuicksort(a, low, left - 2, cmp);
            dualPivotQuicksort(a, right + 2, high, cmp);
            if (left < e1 && e5 < right) {
                while (cmp.compare(a[left], pivot1) == 0) left++;
                while (cmp.compare(a[right], pivot2) == 0) right--;
                outer: for (int k = left; k <= right; k++) {
                    final E ak = a[k];
                    if (cmp.compare(ak, pivot1) == 0) {
                        a[k] = a[left];
                        a[left] = ak;
                        left++;
                    } else if (cmp.compare(ak, pivot2) == 0) {
                        while (cmp.compare(a[right], pivot2) == 0) {
                            if (right-- == k) break outer;
                        }
                        if (cmp.compare(a[right], pivot1) == 0) {
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
            dualPivotQuicksort(a, left, right, cmp);
        } else {
            for (int k = low; k <= right; k++) {
                if (cmp.compare(a[k], pivot1) == 0) continue;
                final E ak = a[k];
                if (cmp.compare(ak, pivot1) < 0) {
                    a[k] = a[left];
                    a[left] = ak;
                    left++;
                } else {
                    while (cmp.compare(a[right], pivot1) > 0) right--;
                    if (cmp.compare(a[right], pivot1) < 0) {
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
            dualPivotQuicksort(a, low, left - 1, cmp);
            dualPivotQuicksort(a, right + 1, high, cmp);
        }
    }
