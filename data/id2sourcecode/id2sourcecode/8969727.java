    protected int dichotoLook(long key, int g, int d) {
        if (this.keys.length == 0) {
            return -1;
        }
        if (g >= d) {
            if (key == this.keys[g]) {
                return g;
            }
            if (g > 0 && key == keys[g - 1]) {
                return g - 1;
            }
            return -1;
        }
        int m = (d + g) / 2;
        if (key <= keys[m]) {
            return dichotoLook(key, g, m);
        }
        return dichotoLook(key, m + 1, d);
    }
