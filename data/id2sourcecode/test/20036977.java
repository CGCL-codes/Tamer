    private static void recursiveQSort(double[] A, int lo, int hi, double[] R) {
        int left = lo;
        int right = hi;
        int middle = (left + right) / 2;
        if (A[left] > A[middle]) refSwap(A, left, middle, R);
        if (A[middle] > A[right]) refSwap(A, middle, right, R);
        if (A[left] > A[middle]) refSwap(A, left, middle, R);
        if ((right - left) > 2) {
            double w = A[middle];
            do {
                while (A[left] < w) left++;
                while (w < A[right]) right--;
                if (left <= right) {
                    refSwap(A, left, right, R);
                    left++;
                    right--;
                }
            } while (left <= right);
            if (lo < right) recursiveQSort(A, lo, right, R);
            if (left < hi) recursiveQSort(A, left, hi, R);
        }
    }
