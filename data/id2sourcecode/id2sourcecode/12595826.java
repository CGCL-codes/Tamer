    private static void shuttlesort(Object from[], Object to[], int low, int high, boolean ascending) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle, ascending);
        shuttlesort(to, from, middle, high, ascending);
        int p = low;
        int q = middle;
        if (high - low >= 4 && compare(from[middle - 1], from[middle], ascending) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }
        for (int i = low; i < high; i++) {
            if (q >= high || p < middle && compare(from[p], from[q], ascending) <= 0) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }
