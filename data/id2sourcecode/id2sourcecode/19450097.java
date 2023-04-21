    boolean canNavigatedByParent(int c) {
        int high = _ParentsSize;
        int low = -1;
        while ((high - low) > 1) {
            int p = (high + low) / 2;
            if (c < _Parents[p]) {
                high = p;
            } else if (_Parents[p] < c) {
                low = p;
            } else return true;
        }
        if (low >= 0 && _Parents[low] == c) return true;
        return false;
    }
