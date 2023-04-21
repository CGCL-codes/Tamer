    public int searchREWF(final int a, final int s, final String p) {
        if (a > s) return (-1 - a);
        int m = (a + s) / 2;
        int r = greaterThanEWF(p, list.get(m));
        if (r == 0) return m;
        if (r == -1) return searchREWF(m + 1, s, p);
        if (a == s) return (-1 - a);
        return searchREWF(a, m - 1, p);
    }
