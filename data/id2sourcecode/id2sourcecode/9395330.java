    static int decomposeIndex(char c) {
        int start = 0;
        int end = DecompositionKeys.k.length / 2;
        while (true) {
            int half = (start + end) / 2;
            int code = DecompositionKeys.k[half * 2];
            if (c == code) {
                return DecompositionKeys.k[half * 2 + 1];
            }
            if (half == start) {
                return -1;
            } else if (c > code) {
                start = half;
            } else {
                end = half;
            }
        }
    }
