    public static int findBlock(char ch) {
        if (blocks == null) {
            return -1;
        }
        int l = blocks.length - 1;
        for (int i = 0; i <= l; ) {
            int p = (l + i) / 2;
            int t = ch - blocks[p];
            if (t == 0) {
                return p;
            }
            if (t < 0) {
                l = --p;
            } else {
                i = ++p;
            }
        }
        return -1;
    }
