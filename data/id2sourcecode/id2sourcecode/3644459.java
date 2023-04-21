    public static void quickSort(Sortable[] inputArray, int arrayBottomIndex, int arrayTopIndex) {
        int lowIndex = arrayBottomIndex;
        int highIndex = arrayTopIndex;
        Sortable pivotValue;
        if (arrayTopIndex > arrayBottomIndex) {
            int pivotIndex = (arrayBottomIndex + arrayTopIndex) / 2;
            pivotValue = inputArray[pivotIndex];
            while (lowIndex < highIndex) {
                while ((lowIndex < pivotIndex) && (inputArray[lowIndex].compareTo(pivotValue) != Sortable.THIS_GREATER_THAN)) {
                    ++lowIndex;
                }
                while ((highIndex > pivotIndex) && (pivotValue.compareTo(inputArray[highIndex]) != Sortable.THIS_GREATER_THAN)) {
                    --highIndex;
                }
                if (lowIndex < highIndex) {
                    inputArray[lowIndex].swap(inputArray[lowIndex], inputArray[highIndex]);
                    if (lowIndex == pivotIndex) {
                        pivotIndex = highIndex;
                        pivotValue = inputArray[highIndex];
                    } else {
                        if (highIndex == pivotIndex) {
                            pivotIndex = lowIndex;
                            pivotValue = inputArray[lowIndex];
                        }
                    }
                }
            }
            if (arrayBottomIndex < pivotIndex) quickSort(inputArray, arrayBottomIndex, pivotIndex - 1);
            if (pivotIndex < arrayTopIndex) quickSort(inputArray, pivotIndex + 1, arrayTopIndex);
        }
    }
