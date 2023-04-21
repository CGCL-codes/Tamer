    static int[] deleteElement(int a[], int del) {
        if (del >= a.length) throw new IllegalArgumentException("Del index " + del + " out of range");
        int b[] = Arrays.copyOf(a, a.length - 1);
        for (int i = del; i < b.length; i++) b[i] = a[i + 1];
        return b;
    }
