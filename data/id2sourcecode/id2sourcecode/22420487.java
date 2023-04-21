    public static void partition(Object[] list, int from, int to, Object[] splitters, int splitFrom, int splitTo, int[] splitIndexes, java.util.Comparator comp) {
        Object splitter;
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
                    l = med3(list, l, l + s, l + 2 * s, comp);
                    m = med3(list, m - s, m, m + s, comp);
                    n = med3(list, n - 2 * s, n - s, n, comp);
                }
                m = med3(list, l, m, n, comp);
            }
            medianIndex = Sorting.binarySearchFromTo(splitters, list[m], splitFrom, splitTo, comp);
            if (medianIndex < 0) medianIndex = -medianIndex - 1;
            if (medianIndex > splitTo) medianIndex = splitTo;
        }
        splitter = splitters[medianIndex];
        int splitIndex = partition(list, from, to, splitter, comp);
        splitIndexes[medianIndex] = splitIndex;
        if (splitIndex < from) {
            int i = medianIndex - 1;
            while (i >= splitFrom && (!(comp.compare(splitter, splitters[i]) < 0))) splitIndexes[i--] = splitIndex;
            splitFrom = medianIndex + 1;
        } else if (splitIndex >= to) {
            int i = medianIndex + 1;
            while (i <= splitTo && (!(comp.compare(splitter, splitters[i]) > 0))) splitIndexes[i++] = splitIndex;
            splitTo = medianIndex - 1;
        }
        if (splitFrom <= medianIndex - 1) {
            partition(list, from, splitIndex, splitters, splitFrom, medianIndex - 1, splitIndexes, comp);
        }
        if (medianIndex + 1 <= splitTo) {
            partition(list, splitIndex + 1, to, splitters, medianIndex + 1, splitTo, splitIndexes, comp);
        }
    }
