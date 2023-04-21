    protected void shuttlesort(int inFrom[], int inTo[], int inLow, int inHigh) {
        if (inHigh - inLow < 2) {
            return;
        }
        int middle = (inLow + inHigh) / 2;
        shuttlesort(inTo, inFrom, inLow, middle);
        shuttlesort(inTo, inFrom, middle, inHigh);
        int p = inLow;
        int q = middle;
        if (inHigh - inLow >= 4 && compare(inFrom[middle - 1], inFrom[middle]) <= 0) {
            for (int i = inLow; i < inHigh; i++) {
                inTo[i] = inFrom[i];
            }
            return;
        }
        for (int i = inLow; i < inHigh; i++) {
            if (q >= inHigh || (p < middle && compare(inFrom[p], inFrom[q]) <= 0)) {
                inTo[i] = inFrom[p++];
            } else {
                inTo[i] = inFrom[q++];
            }
        }
    }
