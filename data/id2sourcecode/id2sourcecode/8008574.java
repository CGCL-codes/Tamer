    private int xxpartition(IntArray iarr, int[] pfl, int left, int right) {
        int mid = (left + right) / 2;
        if (pfl[iarr.elementAt(left)] > pfl[iarr.elementAt(mid)]) xxfswap(iarr, left, mid);
        if (pfl[iarr.elementAt(left)] > pfl[iarr.elementAt(right)]) xxfswap(iarr, left, right);
        if (pfl[iarr.elementAt(mid)] > pfl[iarr.elementAt(right)]) xxfswap(iarr, mid, right);
        int j = right - 1;
        xxfswap(iarr, mid, j);
        int i = left;
        int v = pfl[iarr.elementAt(j)];
        do {
            do {
                i++;
            } while (pfl[iarr.elementAt(i)] < v);
            do {
                j--;
            } while (pfl[iarr.elementAt(j)] > v);
            xxfswap(iarr, i, j);
        } while (i < j);
        xxfswap(iarr, j, i);
        xxfswap(iarr, i, right - 1);
        return i;
    }
