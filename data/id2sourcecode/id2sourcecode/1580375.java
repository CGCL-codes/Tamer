    private static void mergeSort(Comparable[] array, Comparable[] aux, int left, int right) {
        if (left == right) {
            return;
        }
        int middleIndex = (left + right) / 2;
        mergeSort(array, aux, left, middleIndex);
        mergeSort(array, aux, middleIndex + 1, right);
        merge(array, aux, left, right);
        for (int i = left; i <= right; i++) {
            array[i] = aux[i];
        }
    }
