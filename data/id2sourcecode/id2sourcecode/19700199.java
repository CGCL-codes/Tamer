    public void quicksort(int b, int e, Vector a) {
        int i = b, j = e, x = (b + e) / 2;
        do {
            while (cmp(i, x)) i++;
            while (cmp(x, j)) j--;
            if (i <= j) swap(i++, j--, a);
        } while (i < j);
        if (i < e) quicksort(i, e, a);
        if (j > b) quicksort(b, j, a);
    }
