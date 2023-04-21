    private int getIndexForRangeSplit(DynSplitLine[] splitIndices, DynSplitLine root, int min, int max) {
        if (root == splitIndices[min] || root == splitIndices[max]) {
        }
        int mid = (min + max) / 2;
        while (min + 1 < max && splitIndices[mid] != root) {
            if (splitIndices[mid].index.compareTo(root.index) == -1) min = mid; else max = mid;
            mid = (min + max) / 2;
        }
        mid = (min + max) / 2;
        return mid;
    }
