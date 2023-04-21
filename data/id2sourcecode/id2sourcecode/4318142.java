    public int lookupChild(int symbolID, int parentNodeIndex) {
        int low = _nodeFirstChild[parentNodeIndex];
        int high = _nodeFirstChild[parentNodeIndex + 1] - 1;
        if (symbolID < 0) return -1;
        while (low <= high) {
            int mid = (high + low) / 2;
            if (_nodeSymbol[mid] == symbolID) return mid; else if (_nodeSymbol[mid] < symbolID) low = (low == mid ? mid + 1 : mid); else high = (high == mid ? mid - 1 : mid);
        }
        return -1;
    }
