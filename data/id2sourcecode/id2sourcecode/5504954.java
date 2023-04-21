    private int findChildren(Object key) {
        int left = _first;
        int right = _btree._pageSize - 1;
        while (left < right) {
            int middle = (left + right) / 2;
            if (compare(_keys[middle], key) < 0) {
                left = middle + 1;
            } else {
                right = middle;
            }
        }
        return right;
    }
