    private void quickSort(Vector elements, int lowIndex, int highIndex) {
        int lowToHighIndex;
        int highToLowIndex;
        int pivotIndex;
        Comparable pivotValue;
        Comparable lowToHighValue;
        Comparable highToLowValue;
        Comparable parking;
        int newLowIndex;
        int newHighIndex;
        int compareResult;
        lowToHighIndex = lowIndex;
        highToLowIndex = highIndex;
        pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
        pivotValue = (Comparable) elements.elementAt(pivotIndex);
        newLowIndex = highIndex + 1;
        newHighIndex = lowIndex - 1;
        while ((newHighIndex + 1) < newLowIndex) {
            lowToHighValue = (Comparable) elements.elementAt(lowToHighIndex);
            while (lowToHighIndex < newLowIndex & lowToHighValue.compareTo(pivotValue) < 0) {
                newHighIndex = lowToHighIndex;
                lowToHighIndex++;
                lowToHighValue = (Comparable) elements.elementAt(lowToHighIndex);
            }
            highToLowValue = (Comparable) elements.elementAt(highToLowIndex);
            while (newHighIndex <= highToLowIndex & (highToLowValue.compareTo(pivotValue) > 0)) {
                newLowIndex = highToLowIndex;
                highToLowIndex--;
                highToLowValue = (Comparable) elements.elementAt(highToLowIndex);
            }
            if (lowToHighIndex == highToLowIndex) {
                newHighIndex = lowToHighIndex;
            } else if (lowToHighIndex < highToLowIndex) {
                int result = lowToHighValue.compareTo(highToLowValue);
                boolean bCompareResult = (result >= 0);
                if (bCompareResult) {
                    parking = lowToHighValue;
                    elements.setElementAt(highToLowValue, lowToHighIndex);
                    elements.setElementAt(parking, highToLowIndex);
                    newLowIndex = highToLowIndex;
                    newHighIndex = lowToHighIndex;
                    lowToHighIndex++;
                    highToLowIndex--;
                }
            }
        }
        if (lowIndex < newHighIndex) {
            this.quickSort(elements, lowIndex, newHighIndex);
        }
        if (newLowIndex < highIndex) {
            this.quickSort(elements, newLowIndex, highIndex);
        }
    }
