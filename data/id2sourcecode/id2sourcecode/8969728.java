    protected int dichotoLookAfter(long key, int g, int d) {
        if (g >= d) {
            if (key == this.keys[g]) {
                return g;
            }
            if (g > 0 && key == keys[g - 1]) {
                return g - 1;
            }
            return g;
        }
        int m = (d + g) / 2;
        if (key <= keys[m]) {
            return dichotoLookAfter(key, g, m);
        }
        return dichotoLookAfter(key, m + 1, d);
    }
