    private static void quickSort(double[][] wi, int lowIndex, int highIndex, int sobi) {
        int lowToHighIndex, highToLowIndex, pivotIndex;
        double pivotValue, lowToHighValue, highToLowValue;
        double[] parking;
        int newLowIndex, newHighIndex, compareResult;
        lowToHighIndex = lowIndex;
        highToLowIndex = highIndex;
        pivotIndex = (lowToHighIndex + highToLowIndex) / 2;
        pivotValue = wi[pivotIndex][sobi];
        newLowIndex = highIndex + 1;
        newHighIndex = lowIndex - 1;
        while ((newHighIndex + 1) < newLowIndex) {
            lowToHighValue = wi[lowToHighIndex][sobi];
            while (lowToHighIndex < newLowIndex && lowToHighValue < pivotValue) {
                newHighIndex = lowToHighIndex;
                lowToHighIndex++;
                lowToHighValue = wi[lowToHighIndex][sobi];
            }
            highToLowValue = wi[highToLowIndex][sobi];
            while (newHighIndex <= highToLowIndex && highToLowValue > pivotValue) {
                newLowIndex = highToLowIndex;
                highToLowIndex--;
                highToLowValue = wi[highToLowIndex][sobi];
            }
            if (lowToHighIndex == highToLowIndex) {
                newHighIndex = lowToHighIndex;
            } else if (lowToHighIndex < highToLowIndex) {
                if (lowToHighValue >= highToLowValue) {
                    parking = wi[lowToHighIndex];
                    wi[lowToHighIndex] = wi[highToLowIndex];
                    wi[highToLowIndex] = parking;
                    newLowIndex = highToLowIndex;
                    newHighIndex = lowToHighIndex;
                    lowToHighIndex++;
                    highToLowIndex--;
                }
            }
        }
        if (lowIndex < newHighIndex) {
            quickSort(wi, lowIndex, newHighIndex, sobi);
        }
        if (newLowIndex < highIndex) {
            quickSort(wi, newLowIndex, highIndex, sobi);
        }
    }
