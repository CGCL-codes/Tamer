    private static void quicksort(PlaceLocationData array[], int left, int right) {
        int leftIdx = left;
        int rightIdx = right;
        PlaceLocationData temp;
        if (right - left + 1 > 1) {
            int pivot = (left + right) / 2;
            while ((leftIdx <= pivot) && (rightIdx >= pivot)) {
                while ((array[leftIdx].getName().compareTo(array[pivot].getName()) < 0) && (leftIdx <= pivot)) {
                    leftIdx = leftIdx + 1;
                }
                while ((array[rightIdx].getName().compareTo(array[pivot].getName()) > 0) && (rightIdx >= pivot)) {
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
            quicksort(array, left, pivot - 1);
            quicksort(array, pivot + 1, right);
        }
    }
