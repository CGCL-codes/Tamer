    public static short getBondingMar(int elementNumber, int charge) {
        if (charge != 0) {
            short ionic = (short) ((elementNumber << 4) + (charge + 4));
            int iMin = 0, iMax = ionicLookupTable.length;
            while (iMin != iMax) {
                int iMid = (iMin + iMax) / 2;
                if (ionic < ionicLookupTable[iMid]) iMax = iMid; else if (ionic > ionicLookupTable[iMid]) iMin = iMid + 1; else return ionicMars[iMid];
            }
        }
        return (short) covalentMars[elementNumber];
    }
