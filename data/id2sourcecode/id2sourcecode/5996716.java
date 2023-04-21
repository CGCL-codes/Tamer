    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void bottomUpSink(Object[] src, Object element, int r, Comparator c) {
        int j = 0;
        int m = 1;
        while (m < r) {
            if (c.compare(src[m], src[m + 1]) == -1) {
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
        while (j > 0 && c.compare(src[i], element) == -1) {
            src[j] = src[i];
            j = i;
            i = ((j - 1) / 2);
        }
        src[j] = element;
    }
