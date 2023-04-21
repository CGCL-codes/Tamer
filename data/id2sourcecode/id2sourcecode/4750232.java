    private int xxpartition(IntArray iarr, String[] pfl, int left, int right) {
        int mid = (left + right) / 2;
        if (pfl[iarr.elementAt(left)].compareTo(pfl[iarr.elementAt(mid)]) > 0) xxfswap(iarr, left, mid);
        if (pfl[iarr.elementAt(left)].compareTo(pfl[iarr.elementAt(right)]) > 0) xxfswap(iarr, left, right);
        if (pfl[iarr.elementAt(mid)].compareTo(pfl[iarr.elementAt(right)]) > 0) xxfswap(iarr, mid, right);
        int j = right - 1;
        xxfswap(iarr, mid, j);
        int i = left;
        String v = pfl[iarr.elementAt(j)];
        do {
            do {
                i++;
            } while (pfl[iarr.elementAt(i)].compareTo(v) < 0);
            do {
                j--;
            } while (pfl[iarr.elementAt(j)].compareTo(v) > 0);
            xxfswap(iarr, i, j);
        } while (i < j);
        xxfswap(iarr, j, i);
        xxfswap(iarr, i, right - 1);
        return i;
    }
