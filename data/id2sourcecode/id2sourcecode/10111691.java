    private static void mergeSort(Point[] a, int from, int to, Point p) {
        if (from == to) return;
        int mid = (from + to) / 2;
        mergeSort(a, from, mid, p);
        mergeSort(a, mid + 1, to, p);
        merge(a, from, mid, to, p);
    }
