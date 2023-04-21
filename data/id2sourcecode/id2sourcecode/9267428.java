    public void quicksort(int lo, int hi) {
        int i = lo;
        int j = hi;
        Object temp;
        int middleIndex = (lo + hi) / 2;
        Object middle = getObjectAt(middleIndex);
        do {
            while (compare(getObjectAt(i), middle)) i++;
            while (compare(middle, getObjectAt(j))) j--;
            if (i <= j) {
                temp = getObjectAt(i);
                setObjectAt(i, getObjectAt(j));
                setObjectAt(j, temp);
                i++;
                j--;
            }
        } while (i <= j);
        if (lo < j) quicksort(lo, j);
        if (i < hi) quicksort(i, hi);
    }
