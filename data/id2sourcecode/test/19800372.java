    public static int findBlock(char ch) {
        if (TC_TO_SC_LEXEME_BLOCK == null) {
            return -1;
        }
        int l = TC_TO_SC_LEXEME_BLOCK.length - 1;
        for (int i = 0; i <= l; ) {
            int p = (l + i) / 2;
            int t = ch - TC_TO_SC_LEXEME_BLOCK[p];
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
