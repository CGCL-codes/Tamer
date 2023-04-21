    private static int median(Object[] a, int left, int right, Comparator c) {
        int center = (left + right) / 2;
        if (c.compare(a[left], a[center]) > 0) swap(a, left, center);
        if (c.compare(a[left], a[right]) > 0) swap(a, left, right);
        if (c.compare(a[center], a[right]) > 0) swap(a, center, right);
        swap(a, center, right - 1);
        return right - 1;
    }
