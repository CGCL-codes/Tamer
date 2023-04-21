    public void shuttlesort(int from[], int to[], int low, int high, int col) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle, col);
        shuttlesort(to, from, middle, high, col);
        int p = low;
        int q = middle;
        if (high - low >= 4 && sortUpDown(from[middle - 1], from[middle], col) <= 0) {
            for (int i = low; i < high; i++) {
                to[i] = from[i];
            }
            return;
        }
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && sortUpDown(from[p], from[q], col) <= 0)) {
                to[i] = from[p++];
            } else {
                to[i] = from[q++];
            }
        }
    }
