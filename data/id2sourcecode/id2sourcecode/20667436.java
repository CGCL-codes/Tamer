    private static void quicksortnames(NameData array[], int left, int right) {
        int leftIdx = left;
        int rightIdx = right;
        NameData temp;
        if (right - left + 1 > 1) {
            int pivot = (left + right) / 2;
            while ((leftIdx <= pivot) && (rightIdx >= pivot)) {
                while ((array[leftIdx].getCount() < array[pivot].getCount()) && (leftIdx <= pivot)) {
                    leftIdx = leftIdx + 1;
                }
                while ((array[rightIdx].getCount() > array[pivot].getCount()) && (rightIdx >= pivot)) {
                    rightIdx = rightIdx - 1;
                }
                temp = array[leftIdx];
                array[leftIdx] = array[rightIdx];
                array[rightIdx] = temp;
                leftIdx = leftIdx + 1;
                rightIdx = rightIdx - 1;
                if (leftIdx - 1 == pivot) {
                    pivot = rightIdx = rightIdx + 1;
                } else if (rightIdx + 1 == pivot) {
                    pivot = leftIdx = leftIdx - 1;
                }
            }
            quicksortnames(array, left, pivot - 1);
            quicksortnames(array, pivot + 1, right);
        }
    }
