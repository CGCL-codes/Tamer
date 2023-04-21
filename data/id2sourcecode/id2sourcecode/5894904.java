    public static int binarySearchCeil(long k, long[] src) throws NoSuchAlgorithmException {
        int m = 0;
        int end = src.length - 1;
        if (k <= src[end]) {
            int start = 0;
            while (end - start > 1) {
                int t = start + end;
                if (t % 2 == 0) {
                    m = t / 2;
                } else {
                    m = (t + 1) / 2;
                }
                if (src[m] > k) {
                    end = m;
                } else if (src[m] < k) {
                    start = m;
                } else {
                    break;
                }
            }
        }
        return m;
    }
