    public void seekByTimeStamp(int number) {
        System.out.printf("\nTimeStamp slider released at value:  %d\n", number);
        try {
            int currentTS;
            int initialFrame = 1, finalFrame = this.numberOfFrames;
            int median = (initialFrame + finalFrame) / 2;
            int currentSeekPosition = this.timeStampListPointer + this.sizeOfInt * (median - 1);
            System.out.printf("\nMedian : %d, CurrentSeekPosition : %d\n", median, currentSeekPosition);
            while (initialFrame <= finalFrame) {
                randFile.seek(currentSeekPosition);
                currentTS = randFile.readInt();
                System.out.printf("FrameNumber : %d, TimeStamp: %d.\n", median, currentTS);
                System.out.println();
                if (number > currentTS) initialFrame = median + 1; else if (number < currentTS) finalFrame = median - 1; else {
                    initialFrame = median + 1;
                    finalFrame = median - 1;
                }
                median = (initialFrame + finalFrame) / 2;
                currentSeekPosition = this.timeStampListPointer + this.sizeOfInt * (median - 1);
                System.out.printf("Median : %d, CurrentSeekPosition : %d\n", median, currentSeekPosition);
            }
            System.out.printf("\nRequired frame number:  %d\n", median);
            seekByFrameNumber(median);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
