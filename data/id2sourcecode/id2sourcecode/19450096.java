    boolean canNavigateByChild(int c) {
        int high = _ChildrenSize;
        int low = -1;
        while ((high - low) > 1) {
            int p = (high + low) / 2;
            if (c < _Children[p]) {
                high = p;
            } else if (_Children[p] < c) {
                low = p;
            } else return true;
        }
        if (low >= 0 && _Children[low] == c) return true;
        return false;
    }
