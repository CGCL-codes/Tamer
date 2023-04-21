    private int binaryChop(int number, int iValue, int fValue, int[] array) {
        int currentTS;
        int initialValue = 1;
        int finalValue = fValue;
        int median = (initialValue + finalValue) / 2;
        System.out.printf("\nMedian : %d\n", median + iValue - 1);
        while (initialValue <= finalValue) {
            currentTS = array[median - 1];
            System.out.printf("SeekByNumber : %d, TimeStamp: %d.\n", median + iValue - 1, currentTS);
            System.out.println();
            if (number > currentTS) initialValue = median + 1; else if (number < currentTS) finalValue = median - 1; else {
                initialValue = median + 1;
                finalValue = median - 1;
            }
            median = (initialValue + finalValue) / 2;
            System.out.printf("Median : %d\n", median + iValue - 1);
        }
        System.out.printf("\nRequired number:  %d\n", median + iValue - 1);
        return median + iValue - 1;
    }
