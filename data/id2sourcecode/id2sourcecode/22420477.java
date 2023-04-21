    public static void genericPartition(int from, int to, int splitFrom, int splitTo, int[] splitIndexes, IntComparator comp, IntComparator comp2, IntComparator comp3, Swapper swapper) {
        int splitter;
        if (splitFrom > splitTo) return;
        if (from > to) {
            from--;
            for (int i = splitFrom; i <= splitTo; ) splitIndexes[i++] = from;
            return;
        }
        int medianIndex;
        if (splitFrom == splitTo) {
            medianIndex = splitFrom;
        } else {
            int m = (from + to) / 2;
            int len = to - from + 1;
            if (len > SMALL) {
                int l = from;
                int n = to;
                if (len > MEDIUM) {
                    int s = len / 8;
                    l = med3(l, l + s, l + 2 * s, comp2);
                    m = med3(m - s, m, m + s, comp2);
                    n = med3(n - 2 * s, n - s, n, comp2);
                }
                m = med3(l, m, n, comp2);
            }
            medianIndex = binarySearchFromTo(m, splitFrom, splitTo, comp);
            if (medianIndex < 0) medianIndex = -medianIndex - 1;
            if (medianIndex > splitTo) medianIndex = splitTo;
        }
        splitter = medianIndex;
        int splitIndex = genericPartition(from, to, splitter, comp, swapper);
        splitIndexes[medianIndex] = splitIndex;
        if (splitIndex < from) {
            int i = medianIndex - 1;
            while (i >= splitFrom && (!(comp3.compare(splitter, i) < 0))) splitIndexes[i--] = splitIndex;
            splitFrom = medianIndex + 1;
        } else if (splitIndex >= to) {
            int i = medianIndex + 1;
            while (i <= splitTo && (!(comp3.compare(splitter, i) > 0))) splitIndexes[i++] = splitIndex;
            splitTo = medianIndex - 1;
        }
        if (splitFrom <= medianIndex - 1) {
            genericPartition(from, splitIndex, splitFrom, medianIndex - 1, splitIndexes, comp, comp2, comp3, swapper);
        }
        if (medianIndex + 1 <= splitTo) {
            genericPartition(splitIndex + 1, to, medianIndex + 1, splitTo, splitIndexes, comp, comp2, comp3, swapper);
        }
    }
