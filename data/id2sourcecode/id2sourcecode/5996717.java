    private T[] bottomUpSink(T[] src, T element, int r) {
        int j = 0;
        int m = 1;
        while (m < r) {
            if (src[m].compareTo(src[m + 1]) == -1) {
                src[j] = src[m + 1];
                j = m + 1;
            } else {
                src[j] = src[m];
                j = m;
            }
            m = j + j + 1;
        }
        if (m == r) {
            src[j] = src[r];
            j = r;
        }
        int i = ((j - 1) / 2);
        while (j > 0 && src[i].compareTo(element) == -1) {
            src[j] = src[i];
            j = i;
            i = ((j - 1) / 2);
        }
        src[j] = element;
        return src;
    }
