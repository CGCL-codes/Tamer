    private static Comparable median3(List a, int left, int right) {
        int center = (left + right) / 2;
        if (((Comparable) a.get(center)).compareTo(a.get(left)) < 0) swapReferences(a, left, center);
        if (((Comparable) a.get(right)).compareTo(a.get(left)) < 0) swapReferences(a, left, right);
        if (((Comparable) a.get(right)).compareTo(a.get(center)) < 0) swapReferences(a, center, right);
        swapReferences(a, center, right - 1);
        return (Comparable) a.get(right - 1);
    }
