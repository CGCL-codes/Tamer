    private void quickSort(final int mode, final int lowIndex, final int highIndex) {
        final Object[] keys = this.keys;
        final int[] recordIds = this.recordIds;
        int lowToHighIndex;
        int highToLowIndex;
        final int pivotIndex;
        final Object pivotValue;
        Object lowToHighValue;
        Object highToLowValue;
        Object parking;
        int newLowIndex;
        int newHighIndex;
        int compareResult;
        lowToHighIndex = lowIndex;
        highToLowIndex = highIndex;
        pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
        pivotValue = keys[pivotIndex];
        newLowIndex = highIndex + 1;
        newHighIndex = lowIndex - 1;
        while ((newHighIndex + 1) < newLowIndex) {
            lowToHighValue = keys[lowToHighIndex];
            while (lowToHighIndex < newLowIndex && compare(mode, lowToHighValue, pivotValue) < 0) {
                newHighIndex = lowToHighIndex;
                lowToHighIndex++;
                lowToHighValue = keys[lowToHighIndex];
            }
            highToLowValue = keys[highToLowIndex];
            while (newHighIndex <= highToLowIndex && (compare(mode, highToLowValue, pivotValue) > 0)) {
                newLowIndex = highToLowIndex;
                highToLowIndex--;
                highToLowValue = keys[highToLowIndex];
            }
            if (lowToHighIndex == highToLowIndex) {
                newHighIndex = lowToHighIndex;
            } else if (lowToHighIndex < highToLowIndex) {
                compareResult = compare(mode, lowToHighValue, highToLowValue);
                if (compareResult >= 0) {
                    parking = lowToHighValue;
                    final int parkRecordId = recordIds[lowToHighIndex];
                    keys[lowToHighIndex] = highToLowValue;
                    recordIds[lowToHighIndex] = recordIds[highToLowIndex];
                    keys[highToLowIndex] = parking;
                    recordIds[highToLowIndex] = parkRecordId;
                    newLowIndex = highToLowIndex;
                    newHighIndex = lowToHighIndex;
                    lowToHighIndex++;
                    highToLowIndex--;
                }
            }
        }
        if (lowIndex < newHighIndex) {
            this.quickSort(mode, lowIndex, newHighIndex);
        }
        if (newLowIndex < highIndex) {
            this.quickSort(mode, newLowIndex, highIndex);
        }
    }
