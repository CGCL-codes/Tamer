    private void shuttlesort(List from, List to, int low, int high) {
        if (high - low < 2) {
            return;
        }
        int middle = (low + high) / 2;
        shuttlesort(to, from, low, middle);
        shuttlesort(to, from, middle, high);
        int p = low;
        int q = middle;
        if (high - low >= 4 && adjustCompare(getInt(from, middle - 1), getInt(from, middle)) <= 0) {
            for (int i = low; i < high; i++) {
                to.set(i, (Integer) from.get(i));
            }
            return;
        }
        for (int i = low; i < high; i++) {
            if (q >= high || (p < middle && adjustCompare(getInt(from, p), getInt(from, q)) <= 0)) {
                to.set(i, from.get(p++));
            } else {
                to.set(i, from.get(q++));
            }
        }
    }
