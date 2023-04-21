    private static int median(Object[] a, int left, int right) {
        int center = (left + right) / 2;
        if (compare(a[left], a[center]) > 0) swap(a, left, center);
        if (compare(a[left], a[right]) > 0) swap(a, left, right);
        if (compare(a[center], a[right]) > 0) swap(a, center, right);
        swap(a, center, right - 1);
        return right - 1;
    }
