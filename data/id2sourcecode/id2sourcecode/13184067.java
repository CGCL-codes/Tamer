    private int partition(int left, int right) {
        int mid = (left + right) / 2;
        if (((Sortable) array[left]).compareTo((Sortable) array[mid]) > 0) xfswap(array, left, mid);
        if (((Sortable) array[left]).compareTo((Sortable) array[right]) > 0) xfswap(array, left, right);
        if (((Sortable) array[mid]).compareTo((Sortable) array[right]) > 0) xfswap(array, mid, right);
        int j = right - 1;
        xfswap(array, mid, j);
        int i = left;
        Sortable v = (Sortable) array[j];
        do {
            do {
                i++;
            } while (((Sortable) array[i]).compareTo(v) < 0);
            do {
                j--;
            } while (((Sortable) array[j]).compareTo(v) > 0);
            xfswap(array, i, j);
        } while (i < j);
        xfswap(array, j, i);
        xfswap(array, i, right - 1);
        return i;
    }
