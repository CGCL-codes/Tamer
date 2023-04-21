    private final byte matchMonotonicRandom(boolean reversedI, int modeI, int startI, int incrementI, double myDurationI, boolean allowMyHighI, TimeRange otherI, double otherDurationI, boolean allowOtherHighI) {
        int[] numMatches = new int[4];
        boolean[] mineIntersected = new boolean[getNptimes()], otherIntersected = new boolean[otherI.getNptimes()];
        double myLowest = (reversedI ? -(getPtimes()[getNptimes() - 1 - startI] + myDurationI) : getPtimes()[startI]), myHighest = (reversedI ? -getPtimes()[startI] : (getPtimes()[getNptimes() - 1 - startI] + myDurationI)), otherLowest = Double.MAX_VALUE, otherHighest = -Double.MAX_VALUE;
        for (int idx = 0; idx < otherI.getNptimes(); ++idx) {
            double otherLow = (reversedI ? -(otherI.getPtimes()[idx] + otherDurationI) : otherI.getPtimes()[idx]), otherHigh = otherLow + otherDurationI;
            otherLowest = Math.min(otherLow, otherLowest);
            otherHighest = Math.max(otherHigh, otherHighest);
            for (int low = 0, high = getNptimes() - 1, idx1 = (low + high) / 2; (low <= high); idx1 = (low + high) / 2) {
                double myLow = (reversedI ? -(getPtimes()[startI + idx1 * incrementI] + myDurationI) : getPtimes()[startI + idx1 * incrementI]), myHigh = myLow + myDurationI;
                if ((otherLow > myHigh) || (!allowMyHighI && (otherLow == myHigh))) {
                    low = idx1 + 1;
                } else if ((otherHigh < myLow) || (!allowOtherHighI && (otherHigh == myLow))) {
                    high = idx1 - 1;
                } else {
                    compareTimeIntervals(idx1, myLow, myHigh, allowMyHighI, mineIntersected, idx, otherLow, otherHigh, allowOtherHighI, otherIntersected, numMatches);
                    for (int idx2 = idx1 - 1; idx2 >= low; --idx2) {
                        myLow = (reversedI ? -(getPtimes()[startI + idx2 * incrementI] + myDurationI) : getPtimes()[startI + idx2 * incrementI]);
                        myHigh = myLow + myDurationI;
                        if ((myHigh < otherLow) || (!allowMyHighI && (myHigh == otherLow))) {
                            break;
                        }
                        compareTimeIntervals(idx2, myLow, myHigh, allowMyHighI, mineIntersected, idx, otherLow, otherHigh, allowOtherHighI, otherIntersected, numMatches);
                    }
                    for (int idx2 = idx1 + 1; idx2 <= high; ++idx2) {
                        myLow = (reversedI ? -(getPtimes()[startI + idx2 * incrementI] + myDurationI) : getPtimes()[startI + idx2 * incrementI]);
                        myHigh = myLow + myDurationI;
                        if ((myLow > otherHigh) || (!allowOtherHighI && (myLow == otherHigh))) {
                            break;
                        }
                        compareTimeIntervals(idx2, myLow, myHigh, allowMyHighI, mineIntersected, idx, otherLow, otherHigh, allowOtherHighI, otherIntersected, numMatches);
                    }
                }
            }
        }
        return (determineResults(modeI, getNptimes(), myLowest, myHighest, allowMyHighI, mineIntersected, otherI.getNptimes(), otherLowest, otherHighest, allowOtherHighI, otherIntersected, numMatches));
    }
