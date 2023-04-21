    private void quicksort(int[] dims, double[] vals, int low, int high) {
        if (low >= high) return;
        int leftIdx = low;
        int pivot = low;
        int rightIdx = high;
        pivot = (low + high) / 2;
        while (leftIdx <= pivot && rightIdx >= pivot) {
            while (dims[leftIdx] < dims[pivot] && leftIdx <= pivot) leftIdx++;
            while (dims[rightIdx] > dims[pivot] && rightIdx >= pivot) rightIdx--;
            int tmp = dims[leftIdx];
            dims[leftIdx] = dims[rightIdx];
            dims[rightIdx] = tmp;
            double tmpd = vals[leftIdx];
            vals[leftIdx] = vals[rightIdx];
            vals[rightIdx] = tmpd;
            leftIdx++;
            rightIdx--;
            if (leftIdx - 1 == pivot) pivot = rightIdx = rightIdx + 1; else if (rightIdx + 1 == pivot) pivot = leftIdx = leftIdx - 1;
            quicksort(dims, vals, low, pivot - 1);
            quicksort(dims, vals, pivot + 1, high);
        }
    }
