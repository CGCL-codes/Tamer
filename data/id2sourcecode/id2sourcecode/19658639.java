    private static void dualPivotQuicksort(char[] a, int left, int right) {
        int sixth = (right - left + 1) / 6;
        int e1 = left + sixth;
        int e5 = right - sixth;
        int e3 = (left + right) >>> 1;
        int e4 = e3 + sixth;
        int e2 = e3 - sixth;
        char ae1 = a[e1], ae2 = a[e2], ae3 = a[e3], ae4 = a[e4], ae5 = a[e5];
        if (ae1 > ae2) {
            char t = ae1;
            ae1 = ae2;
            ae2 = t;
        }
        if (ae4 > ae5) {
            char t = ae4;
            ae4 = ae5;
            ae5 = t;
        }
        if (ae1 > ae3) {
            char t = ae1;
            ae1 = ae3;
            ae3 = t;
        }
        if (ae2 > ae3) {
            char t = ae2;
            ae2 = ae3;
            ae3 = t;
        }
        if (ae1 > ae4) {
            char t = ae1;
            ae1 = ae4;
            ae4 = t;
        }
        if (ae3 > ae4) {
            char t = ae3;
            ae3 = ae4;
            ae4 = t;
        }
        if (ae2 > ae5) {
            char t = ae2;
            ae2 = ae5;
            ae5 = t;
        }
        if (ae2 > ae3) {
            char t = ae2;
            ae2 = ae3;
            ae3 = t;
        }
        if (ae4 > ae5) {
            char t = ae4;
            ae4 = ae5;
            ae5 = t;
        }
        a[e1] = ae1;
        a[e3] = ae3;
        a[e5] = ae5;
        char pivot1 = ae2;
        a[e2] = a[left];
        char pivot2 = ae4;
        a[e4] = a[right];
        int less = left + 1;
        int great = right - 1;
        boolean pivotsDiffer = (pivot1 != pivot2);
        if (pivotsDiffer) {
            outer: for (int k = less; k <= great; k++) {
                char ak = a[k];
                if (ak < pivot1) {
                    if (k != less) {
                        a[k] = a[less];
                        a[less] = ak;
                    }
                    less++;
                } else if (ak > pivot2) {
                    while (a[great] > pivot2) {
                        if (great-- == k) {
                            break outer;
                        }
                    }
                    if (a[great] < pivot1) {
                        a[k] = a[less];
                        a[less++] = a[great];
                        a[great--] = ak;
                    } else {
                        a[k] = a[great];
                        a[great--] = ak;
                    }
                }
            }
        } else {
            for (int k = less; k <= great; k++) {
                char ak = a[k];
                if (ak == pivot1) {
                    continue;
                }
                if (ak < pivot1) {
                    if (k != less) {
                        a[k] = a[less];
                        a[less] = ak;
                    }
                    less++;
                } else {
                    while (a[great] > pivot1) {
                        great--;
                    }
                    if (a[great] < pivot1) {
                        a[k] = a[less];
                        a[less++] = a[great];
                        a[great--] = ak;
                    } else {
                        a[k] = pivot1;
                        a[great--] = ak;
                    }
                }
            }
        }
        a[left] = a[less - 1];
        a[less - 1] = pivot1;
        a[right] = a[great + 1];
        a[great + 1] = pivot2;
        doSort(a, left, less - 2);
        doSort(a, great + 2, right);
        if (!pivotsDiffer) {
            return;
        }
        if (less < e1 && great > e5) {
            while (a[less] == pivot1) {
                less++;
            }
            while (a[great] == pivot2) {
                great--;
            }
            outer: for (int k = less; k <= great; k++) {
                char ak = a[k];
                if (ak == pivot2) {
                    while (a[great] == pivot2) {
                        if (great-- == k) {
                            break outer;
                        }
                    }
                    if (a[great] == pivot1) {
                        a[k] = a[less];
                        a[less++] = pivot1;
                    } else {
                        a[k] = a[great];
                    }
                    a[great--] = pivot2;
                } else if (ak == pivot1) {
                    a[k] = a[less];
                    a[less++] = pivot1;
                }
            }
        }
        doSort(a, less, great);
    }
