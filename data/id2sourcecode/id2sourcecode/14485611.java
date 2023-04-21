    private int binaryChop(int number, int iValue, int fValue, long indexPointer) {
        int currentTS;
        int initialValue = iValue;
        int finalValue = fValue;
        int median = (initialValue + finalValue) / 2;
        long currentSeekPosition = indexPointer + sizeOfInt * (median - iValue);
        System.out.printf("\nMedian : %d, CurrentSeekPosition : %d\n", median, currentSeekPosition);
        try {
            while (initialValue <= finalValue) {
                randFile.seek(currentSeekPosition);
                currentTS = randFile.readInt();
                System.out.printf("SeekByNumber : %d, TimeStamp: %d.\n", median, currentTS);
                System.out.println();
                if (number > currentTS) initialValue = median + 1; else if (number < currentTS) finalValue = median - 1; else {
                    initialValue = median + 1;
                    finalValue = median - 1;
                }
                median = (initialValue + finalValue) / 2;
                currentSeekPosition = indexPointer + sizeOfInt * (median - iValue);
                System.out.printf("Median : %d, CurrentSeekPosition : %d\n", median, currentSeekPosition);
            }
        } catch (IOException e) {
            System.err.println(e + " caught inside binaryChop()");
        }
        System.out.printf("\nRequired number:  %d\n", median);
        return median;
    }
