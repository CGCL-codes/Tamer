    public static short getEdgeingMar(int nodeicNumber, int charge, short[] table) {
        short ionic = (short) ((nodeicNumber << 4) + (charge + 4));
        int iVal = 0, iMid = 0, iMin = 0, iMax = table.length / 2;
        while (iMin != iMax) {
            iMid = (iMin + iMax) / 2;
            iVal = table[iMid << 1];
            if (iVal > ionic) iMax = iMid; else if (iVal < ionic) iMin = iMid + 1; else return table[(iMid << 1) + 1];
        }
        if (iVal > ionic) iMid--;
        iVal = table[iMid << 1];
        if (nodeicNumber != (iVal >> 4)) iMid++;
        return table[(iMid << 1) + 1];
    }
