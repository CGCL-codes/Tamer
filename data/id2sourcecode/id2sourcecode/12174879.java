    private final byte matchMonotonic2(boolean reversedI, int modeI, int startI, int incrementI, double myDurationI, boolean allowMyHighI, int otherStartI, int otherIncrementI, TimeRange otherI, double otherDurationI, boolean allowOtherHighI) {
        int currentLow = 0;
        int[] numMatches = new int[4];
        boolean[] mineIntersected = new boolean[getNptimes()], otherIntersected = new boolean[otherI.getNptimes()];
        double myLowest = (reversedI ? -(getPtimes()[getNptimes() - 1 - startI] + myDurationI) : getPtimes()[startI]), myHighest = (reversedI ? -(getPtimes()[getNptimes() - 1 - startI] + myDurationI) : getPtimes()[startI]), otherLowest = (reversedI ? -(otherI.getPtimes()[otherI.getNptimes() - 1 - otherStartI] + otherDurationI) : otherI.getPtimes()[otherStartI]), otherHighest = (reversedI ? -otherI.getPtimes()[otherStartI] : (otherI.getPtimes()[otherI.getNptimes() - 1 - otherStartI] + otherDurationI));
        for (int tIdx = 0, idx = otherStartI; (tIdx < otherI.getNptimes()); ++tIdx, idx += otherIncrementI) {
            double otherLow = (reversedI ? -(otherI.getPtimes()[idx] + otherDurationI) : otherI.getPtimes()[idx]), otherHigh = otherLow + otherDurationI;
            for (int low = currentLow, high = getNptimes() - 1, idx1 = (low + high) / 2; (low <= high); idx1 = (low + high) / 2) {
                double myLow = (reversedI ? -(getPtimes()[startI + idx1 * incrementI] + myDurationI) : getPtimes()[startI + idx1 * incrementI]), myHigh = myLow + myDurationI;
                if ((otherLow > myHigh) || (!allowMyHighI && (otherLow == myHigh))) {
                    currentLow = low = idx1 + 1;
                } else if ((otherHigh < myLow) || (!allowOtherHighI && (otherHigh == myLow))) {
                    high = idx1 - 1;
                } else {
                    compareTimeIntervals(idx1, myLow, myHigh, allowMyHighI, mineIntersected, idx, otherLow, otherHigh, allowOtherHighI, otherIntersected, numMatches);
                    int idx2;
                    for (idx2 = idx1 - 1; idx2 >= low; --idx2) {
                        myLow = (reversedI ? -(getPtimes()[startI + idx2 * incrementI] + myDurationI) : getPtimes()[startI + idx2 * incrementI]);
                        myHigh = myLow + myDurationI;
                        if ((myHigh < otherLow) || (!allowMyHighI && (myHigh == otherLow))) {
                            break;
                        }
                        compareTimeIntervals(idx2, myLow, myHigh, allowMyHighI, mineIntersected, idx, otherLow, otherHigh, allowOtherHighI, otherIntersected, numMatches);
                    }
                    currentLow = idx2 + 1;
                    for (idx2 = idx1 + 1; idx2 <= high; ++idx2) {
                        myLow = (reversedI ? -(getPtimes()[startI + idx2 * incrementI] + myDurationI) : getPtimes()[startI + idx2 * incrementI]);
                        myHigh = myLow + myDurationI;
                        if ((myLow > otherHigh) || (!allowOtherHighI && (myLow == otherHigh))) {
                            break;
                        }
                        compareTimeIntervals(idx2, myLow, myHigh, allowMyHighI, mineIntersected, idx, otherLow, otherHigh, allowOtherHighI, otherIntersected, numMatches);
                    }
                    break;
                }
            }
        }
        byte resultR = determineResults(modeI, getNptimes(), myLowest, myHighest, allowMyHighI, mineIntersected, otherI.getNptimes(), otherLowest, otherHighest, allowOtherHighI, otherIntersected, numMatches);
        return (resultR);
    }
