    private final int median(final int left, final int right) {
        final int center = (left + right) / 2;
        if (compare(left, center) > 0) swap(left, center);
        if (compare(left, right) > 0) swap(left, right);
        if (compare(center, right) > 0) swap(center, right);
        swap(center, right - 1);
        return right - 1;
    }
