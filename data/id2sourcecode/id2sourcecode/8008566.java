    private int partition(int left, int right) {
        int mid = (left + right) / 2;
        if (array[left] > array[mid]) xfswap(array, left, mid);
        if (array[left] > array[right]) xfswap(array, left, right);
        if (array[mid] > array[right]) xfswap(array, mid, right);
        int j = right - 1;
        xfswap(array, mid, j);
        int i = left;
        int v = array[j];
        do {
            do {
                i++;
            } while (array[i] < v);
            do {
                j--;
            } while (array[j] > v);
            xfswap(array, i, j);
        } while (i < j);
        xfswap(array, j, i);
        xfswap(array, i, right - 1);
        return i;
    }
