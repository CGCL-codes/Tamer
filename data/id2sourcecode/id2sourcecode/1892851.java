    public void shuttlesort(int[] from, int[] to, int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);
        int p = low;
        int q = middle;
        if ((high - low >= 4) && (compare(from[(middle - 1)], from[middle]) <= 0)) {
            for (int i = low; i < high; ++i) {
                to[i] = from[i];
            }
            return;
        }
        for (int i = low; i < high; ++i) if ((q >= high) || ((p < middle) && (compare(from[p], from[q]) <= 0))) {
            to[i] = from[(p++)];
        } else to[i] = from[(q++)];
    }
