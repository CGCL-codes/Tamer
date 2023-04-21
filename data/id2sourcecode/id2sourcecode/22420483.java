    public static void partition(double[] list, int from, int to, double[] splitters, int splitFrom, int splitTo, int[] splitIndexes) {
        double splitter;
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
                    l = med3(list, l, l + s, l + 2 * s);
                    m = med3(list, m - s, m, m + s);
                    n = med3(list, n - 2 * s, n - s, n);
                }
                m = med3(list, l, m, n);
            }
            medianIndex = Sorting.binarySearchFromTo(splitters, list[m], splitFrom, splitTo);
            if (medianIndex < 0) medianIndex = -medianIndex - 1;
            if (medianIndex > splitTo) medianIndex = splitTo;
        }
        splitter = splitters[medianIndex];
        int splitIndex = partition(list, from, to, splitter);
        splitIndexes[medianIndex] = splitIndex;
        if (splitIndex < from) {
            int i = medianIndex - 1;
            while (i >= splitFrom && (!(splitter < splitters[i]))) splitIndexes[i--] = splitIndex;
            splitFrom = medianIndex + 1;
        } else if (splitIndex >= to) {
            int i = medianIndex + 1;
            while (i <= splitTo && (!(splitter > splitters[i]))) splitIndexes[i++] = splitIndex;
            splitTo = medianIndex - 1;
        }
        if (splitFrom <= medianIndex - 1) {
            partition(list, from, splitIndex, splitters, splitFrom, medianIndex - 1, splitIndexes);
        }
        if (medianIndex + 1 <= splitTo) {
            partition(list, splitIndex + 1, to, splitters, medianIndex + 1, splitTo, splitIndexes);
        }
    }
