    public void leftShift(int from, int to) {
        structure = null;
        Path first = genes[from];
        for (int i = from; i < to; ++i) {
            genes[i] = genes[i + 1];
        }
        genes[to] = first;
    }
