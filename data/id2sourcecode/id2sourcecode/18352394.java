    private static void binaryInsertionSort(int[] a) {
        int tmp = 0;
        int left = 0;
        int right = 0;
        int mid = 0;
        for (int i = 1; i < a.length; i++) {
            tmp = a[i];
            right = i;
            while (left < right) {
                mid = (left + right) / 2;
                if (tmp >= a[mid]) {
                    left = mid + 1;
                } else {
                    right = mid;
                }
            }
            for (int j = i; j > left; j--) {
                swap(a, j);
            }
        }
    }
