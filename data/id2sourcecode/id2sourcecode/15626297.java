    public static void quickSort(Object s[], int lo, int hi, TwoBlock block) {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        if (((Integer) block.value(s[lo], s[mid])).intValue() > 0) {
            Object tmp = s[lo];
            s[lo] = s[mid];
            s[mid] = tmp;
        }
        if (((Integer) block.value(s[mid], s[hi])).intValue() > 0) {
            Object tmp = s[mid];
            s[mid] = s[hi];
            s[hi] = tmp;
            if (((Integer) block.value(s[lo], s[mid])).intValue() > 0) {
                Object tmp2 = s[lo];
                s[lo] = s[mid];
                s[mid] = tmp2;
            }
        }
        int left = lo + 1;
        int right = hi - 1;
        if (left >= right) {
            return;
        }
        Object partition = s[mid];
        for (; ; ) {
            while (((Integer) block.value(s[right], partition)).intValue() > 0) {
                --right;
            }
            while (left < right && ((Integer) block.value(s[left], partition)).intValue() <= 0) {
                ++left;
            }
            if (left < right) {
                Object tmp = s[left];
                s[left] = s[right];
                s[right] = tmp;
                --right;
            } else {
                break;
            }
        }
        quickSort(s, lo, left, block);
        quickSort(s, left + 1, hi, block);
    }
