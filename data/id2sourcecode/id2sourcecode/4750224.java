    private int partition(int left, int right) {
        int mid = (left + right) / 2;
        if (elem[left].compareTo(elem[mid]) > 0) xfswap(elem, left, mid);
        if (elem[left].compareTo(elem[right]) > 0) xfswap(elem, left, right);
        if (elem[mid].compareTo(elem[right]) > 0) xfswap(elem, mid, right);
        int j = right - 1;
        xfswap(elem, mid, j);
        int i = left;
        String v = elem[j];
        do {
            do {
                i++;
            } while (elem[i].compareTo(v) < 0);
            do {
                j--;
            } while (elem[j].compareTo(v) > 0);
            xfswap(elem, i, j);
        } while (i < j);
        xfswap(elem, j, i);
        xfswap(elem, i, right - 1);
        return i;
    }
