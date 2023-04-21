    String[] deleteFirst(String[] a) {
        assert a.length > 0;
        String[] b = new String[a.length - 1];
        for (int i = 0; i < b.length; i++) b[i] = a[i + 1];
        return b;
    }
