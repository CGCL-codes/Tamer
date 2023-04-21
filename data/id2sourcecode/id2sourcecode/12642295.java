    private static void quickSort(Object[] arr, Comparer c, int from, int to) {
        if (to <= from) return;
        int mid = (from + to) / 2;
        if (mid != from) swap(arr, mid, from);
        Object pivot = arr[from];
        int highestBelowPivot = from - 1;
        int low = from + 1;
        int high = to;
        while (low <= high) {
            int cmp = c.compare(arr[low], pivot);
            if (cmp <= 0) {
                if (cmp < 0) {
                    highestBelowPivot = low;
                }
                low++;
            } else {
                int c2;
                for (; ; ) {
                    c2 = c.compare(arr[high], pivot);
                    if (c2 > 0) {
                        high--;
                        if (low > high) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (low <= high) {
                    swap(arr, low, high);
                    if (c2 < 0) {
                        highestBelowPivot = low;
                    }
                    low++;
                    high--;
                }
            }
        }
        if (highestBelowPivot > from) {
            swap(arr, from, highestBelowPivot);
            quickSort(arr, c, from, highestBelowPivot - 1);
        }
        quickSort(arr, c, high + 1, to);
    }
