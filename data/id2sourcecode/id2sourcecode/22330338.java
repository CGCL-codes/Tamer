    private int partition(int l, int r) {
        int m = (l + r) / 2;
        while (l <= r) {
            while (s.compare(m, r) < 0) r--;
            while (s.compare(l, m) < 0) l++;
            if (l < r) {
                if (l == m) m = r; else if (r == m) m = l;
                s.swap(l++, r--);
            } else return r;
        }
        return r;
    }
