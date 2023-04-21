    private static Comparable median3(Comparable[] a, int left, int right) {
        int center = (left + right) / 2;
        if (a[center].compareTo(a[left]) < 0) swapReferences(a, left, center);
        if (a[right].compareTo(a[left]) < 0) swapReferences(a, left, right);
        if (a[right].compareTo(a[center]) < 0) swapReferences(a, center, right);
        swapReferences(a, center, right - 1);
        return a[right - 1];
    }
