    protected int[] getCompletionsBin(String start, AbstractEntry[] entries, int[] initBounds) {
        int[] bounds = new int[] { -1, -1 };
        int left = initBounds[0], right = initBounds[1] - 1;
        int middle = right / 2;
        if (left > right) return bounds;
        if (entries[left].key.startsWith(start)) right = middle = left;
        while (left < middle) {
            if (entries[middle].key.compareTo(start) >= 0) {
                right = middle;
                middle = (left + middle) / 2;
            } else {
                left = middle;
                middle = (middle + right) / 2;
            }
        }
        if (!entries[right].key.startsWith(start)) return bounds;
        bounds[0] = right;
        left = right;
        right = initBounds[1] - 1;
        if (entries[right].key.startsWith(start)) {
            bounds[1] = right + 1;
            return bounds;
        }
        middle = (left + right) / 2;
        while (left < middle) {
            if (entries[middle].key.startsWith(start)) {
                left = middle;
                middle = (right + middle) / 2;
            } else {
                right = middle;
                middle = (middle + left) / 2;
            }
        }
        bounds[1] = right;
        return bounds;
    }
