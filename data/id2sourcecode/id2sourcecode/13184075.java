    private int xxpartition(IntArray iarr, Object[] pfl, int left, int right) {
        int mid = (left + right) / 2;
        if (((Sortable) pfl[iarr.elementAt(left)]).compareTo((Sortable) pfl[iarr.elementAt(mid)]) > 0) xxfswap(iarr, left, mid);
        if (((Sortable) pfl[iarr.elementAt(left)]).compareTo((Sortable) pfl[iarr.elementAt(right)]) > 0) xxfswap(iarr, left, right);
        if (((Sortable) pfl[iarr.elementAt(mid)]).compareTo((Sortable) pfl[iarr.elementAt(right)]) > 0) xxfswap(iarr, mid, right);
        int j = right - 1;
        xxfswap(iarr, mid, j);
        int i = left;
        Sortable v = (Sortable) pfl[iarr.elementAt(j)];
        do {
            do {
                i++;
            } while (((Sortable) pfl[iarr.elementAt(i)]).compareTo(v) < 0);
            do {
                j--;
            } while (((Sortable) pfl[iarr.elementAt(j)]).compareTo(v) > 0);
            xxfswap(iarr, i, j);
        } while (i < j);
        xxfswap(iarr, j, i);
        xxfswap(iarr, i, right - 1);
        return i;
    }
