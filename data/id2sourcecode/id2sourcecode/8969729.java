    protected int dichotoLookBefore(long key, int g, int d) {
        if (g >= d) {
            if (key == this.keys[g]) {
                return g;
            }
            if (g > 0 && key == keys[g - 1]) {
                return g - 1;
            }
            return (g == 0) ? 0 : (g - 1);
        }
        int m = (d + g) / 2;
        if (key <= keys[m]) {
            return dichotoLookBefore(key, g, m);
        }
        return dichotoLookBefore(key, m + 1, d);
    }
