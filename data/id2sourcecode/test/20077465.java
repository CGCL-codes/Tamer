    public void quicksort(String[] arr, int lo, int hi) {
        if (lo >= hi) {
            return;
        }
        int mid = (lo + hi) / 2;
        String tmp;
        String middle = arr[mid];
        if (arr[lo].compareTo(middle) > 0) {
            arr[mid] = arr[lo];
            arr[lo] = middle;
            middle = arr[mid];
        }
        if (middle.compareTo(arr[hi]) > 0) {
            arr[mid] = arr[hi];
            arr[hi] = middle;
            middle = arr[mid];
            if (arr[lo].compareTo(middle) > 0) {
                arr[mid] = arr[lo];
                arr[lo] = middle;
                middle = arr[mid];
            }
        }
        int left = lo + 1;
        int right = hi - 1;
        if (left >= right) {
            return;
        }
        for (; ; ) {
            while (arr[right].compareTo(middle) > 0) {
                right--;
            }
            while (left < right && arr[left].compareTo(middle) <= 0) {
                left++;
            }
            if (left < right) {
                tmp = arr[left];
                arr[left] = arr[right];
                arr[right] = tmp;
                right--;
            } else {
                break;
            }
        }
        quicksort(arr, lo, left);
        quicksort(arr, left + 1, hi);
    }
