    private static void merge(Comparable[] array, Comparable[] aux, int left, int right) {
        int middleIndex = (left + right) / 2;
        int leftIndex = left;
        int rightIndex = middleIndex + 1;
        int auxIndex = left;
        while (leftIndex <= middleIndex && rightIndex <= right) {
            if (array[leftIndex].compareTo(array[rightIndex]) != -1) {
                aux[auxIndex] = array[leftIndex++];
            } else {
                aux[auxIndex] = array[rightIndex++];
            }
            auxIndex++;
        }
        while (leftIndex <= middleIndex) {
            aux[auxIndex] = array[leftIndex++];
            auxIndex++;
        }
        while (rightIndex <= right) {
            aux[auxIndex] = array[rightIndex++];
            auxIndex++;
        }
    }
