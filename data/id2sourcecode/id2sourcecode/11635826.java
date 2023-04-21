    public int luaLength() {
        int i = 0;
        int j = array.length;
        if (j <= 0 || containsKey(j)) {
            if (hashKeys.length == 0) return j;
            for (++j; containsKey(j) && j < MAX_KEY; j *= 2) i = j;
        }
        while (j - i > 1) {
            int m = (i + j) / 2;
            if (!containsKey(m)) j = m; else i = m;
        }
        return i;
    }
