    private int[] generateDownladOrder(final int size) {
        int[] dindex = new int[size];
        if (size < 4) {
            for (int i = 0; i < dindex.length; i++) {
                dindex[i] = i;
            }
            return dindex;
        }
        boolean[] map = new boolean[size];
        int pos = 0;
        dindex[pos++] = 0;
        map[0] = true;
        dindex[pos++] = size - 1;
        map[size - 1] = true;
        int k = (size - 1) / 2;
        dindex[pos++] = k;
        map[k] = true;
        while (k > 0) {
            int i = 1;
            int start = 0;
            while (i < map.length) {
                if (map[i]) {
                    if (!map[i - 1]) {
                        int mid = start + (i - start) / 2;
                        map[mid] = true;
                        dindex[pos++] = mid;
                    }
                    start = i;
                }
                i++;
            }
            k /= 2;
        }
        return dindex;
    }
