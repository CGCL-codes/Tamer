    public static int[] keyLeftMove(int[] source, int i) {
        int temp = 0;
        int len = source.length;
        int ls = LS[i];
        for (int k = 0; k < ls; k++) {
            temp = source[0];
            for (int j = 0; j < len - 1; j++) {
                source[j] = source[j + 1];
            }
            source[len - 1] = temp;
        }
        return source;
    }
