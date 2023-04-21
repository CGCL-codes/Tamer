    private void sort(int[][] sorted) {
        for (int i = 1; i < sorted.length; i++) {
            int left = 0;
            int right = i;
            int[] temp = sorted[i];
            while (left < right) {
                int middle = (left + right) / 2;
                if (sorted[middle][0] <= temp[0]) {
                    left = middle + 1;
                } else {
                    right = middle;
                }
            }
            for (int j = i; j > right; j--) {
                sorted[j] = sorted[j - 1];
            }
            sorted[right] = temp;
        }
    }
