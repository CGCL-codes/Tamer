    void insertSortedData(TstItem[] items, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            insert(items[m].key, items[m].value);
            insertSortedData(items, l, m);
            insertSortedData(items, m + 1, r);
        }
    }
