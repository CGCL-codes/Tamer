    public static void mergeSort(int[] a, int left, int right) {
        if (left >= right) {
            return;
        }
        int mid = (left + right) / 2;
        mergeSort(a, left, mid);
        mergeSort(a, mid + 1, right);
        int[] b = new int[a.length];
        int i = 0;
        int j = 0;
        for (i = mid + 1; i > left; i--) {
            b[i - 1] = a[i - 1];
        }
        for (j = mid; j <= right - 1; j++) {
            b[right + mid - j] = a[j + 1];
        }
        for (int k = left; k <= right; k++) {
            if (b[i] < b[j]) {
                a[k] = b[i];
                i++;
            } else if (b[i] > b[j]) {
                a[k] = b[j];
                j--;
            }
        }
    }
