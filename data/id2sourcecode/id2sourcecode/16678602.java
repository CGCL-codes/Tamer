    @SuppressWarnings("unchecked")
    <T> T[] deleteDisElement(T[] a) {
        T[] b = (T[]) new Object[a.length - 1];
        for (int i = 0; i < did; i++) b[i] = a[i];
        for (int i = did; i < b.length; i++) b[i] = a[i + 1];
        return b;
    }
