    private static void QuickSort(ArrayList arr, int lo, int hi, Comparator comparator) {
        if (lo >= hi) return;
        int mid = (lo + hi) / 2;
        Object tmp;
        Object middle = arr.get(mid);
        if (comparator.compare(arr.get(lo), middle) > 0) {
            arr.set(mid, arr.get(lo));
            arr.set(lo, middle);
            middle = arr.get(mid);
        }
        if (comparator.compare(middle, arr.get(hi)) > 0) {
            arr.set(mid, arr.get(hi));
            arr.set(hi, middle);
            middle = arr.get(mid);
            if (comparator.compare(arr.get(lo), middle) > 0) {
                arr.set(mid, arr.get(lo));
                arr.set(lo, middle);
                middle = arr.get(mid);
            }
        }
        int left = lo + 1;
        int right = hi - 1;
        if (left >= right) return;
        for (; ; ) {
            while (comparator.compare(arr.get(right), middle) > 0) {
                right--;
            }
            while (left < right && comparator.compare(arr.get(left), middle) <= 0) {
                left++;
            }
            if (left < right) {
                tmp = arr.get(left);
                arr.set(left, arr.get(right));
                arr.set(right, tmp);
                right--;
            } else {
                break;
            }
        }
        QuickSort(arr, lo, left, comparator);
        QuickSort(arr, left + 1, hi, comparator);
    }
