    public void seekByTimeStamp(int number) {
        System.out.printf("\nTimeStamp slider released at value:  %d\n", number);
        int currentTS;
        int initialFrame = 1, finalFrame = this.numberOfFrames;
        int median = (initialFrame + finalFrame) / 2;
        while (initialFrame <= finalFrame) {
            currentTS = this.timeStampList[median - 1];
            if (number > currentTS) initialFrame = median + 1; else if (number < currentTS) finalFrame = median - 1; else {
                initialFrame = median + 1;
                finalFrame = median - 1;
            }
            median = (initialFrame + finalFrame) / 2;
        }
        seekByFrameNumber(median);
    }
