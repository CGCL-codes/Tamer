    private static int wsrch(String word, int low, int high) {
        int i, j;
        word = word.toUpperCase();
        for (; ; ) {
            i = (low + high) / 2;
            if ((j = word.compareTo(dict[i])) == 0) {
                return i;
            } else if (high == low + 1) {
                if (word.equalsIgnoreCase(dict[high])) return high; else return -1;
            } else if (low >= high) {
                return -1;
            }
            if (j < 0) high = i; else low = i;
        }
    }
