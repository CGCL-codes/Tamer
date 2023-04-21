    public final void ms(int from, int to) {
        if (from < to) {
            int middle = (from + to) / 2;
            ms(from, middle);
            ms(middle + 1, to);
            sortedInsert(from, middle, to);
        }
    }
