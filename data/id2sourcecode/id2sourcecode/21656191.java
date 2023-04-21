    public static final void quickSort(JPLITheoMSPeak[] peaks, int lowIndex, int highIndex) {
        int lowToHighIndex;
        int highToLowIndex;
        int pivotIndex;
        JPLITheoMSPeak pivotValue;
        JPLITheoMSPeak lowToHighValue;
        JPLITheoMSPeak highToLowValue;
        JPLITheoMSPeak parking;
        int newLowIndex;
        int newHighIndex;
        int compareResult;
        lowToHighIndex = lowIndex;
        highToLowIndex = highIndex;
        pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
        pivotValue = peaks[pivotIndex];
        newLowIndex = highIndex + 1;
        newHighIndex = lowIndex - 1;
        while ((newHighIndex + 1) < newLowIndex) {
            lowToHighValue = peaks[lowToHighIndex];
            while (lowToHighIndex < newLowIndex & lowToHighValue.compareTo(pivotValue) < 0) {
                newHighIndex = lowToHighIndex;
                lowToHighIndex++;
                lowToHighValue = peaks[lowToHighIndex];
            }
            highToLowValue = peaks[highToLowIndex];
            while (newHighIndex <= highToLowIndex & (highToLowValue.compareTo(pivotValue) > 0)) {
                newLowIndex = highToLowIndex;
                highToLowIndex--;
                highToLowValue = peaks[highToLowIndex];
            }
            if (lowToHighIndex == highToLowIndex) {
                newHighIndex = lowToHighIndex;
            } else if (lowToHighIndex < highToLowIndex) {
                compareResult = lowToHighValue.compareTo(highToLowValue);
                if (compareResult >= 0) {
                    parking = lowToHighValue;
                    peaks[lowToHighIndex] = highToLowValue;
                    peaks[highToLowIndex] = parking;
                    newLowIndex = highToLowIndex;
                    newHighIndex = lowToHighIndex;
                    lowToHighIndex++;
                    highToLowIndex--;
                }
            }
        }
        if (lowIndex < newHighIndex) {
            quickSort(peaks, lowIndex, newHighIndex);
        }
        if (newLowIndex < highIndex) {
            quickSort(peaks, newLowIndex, highIndex);
        }
    }
