    public static int findBlock(char ch) {
        if (SC_TO_TC_LEXEME_BLOCK == null) {
            return -1;
        }
        int l = SC_TO_TC_LEXEME_BLOCK.length - 1;
        for (int i = 0; i <= l; ) {
            int p = (l + i) / 2;
            int t = ch - SC_TO_TC_LEXEME_BLOCK[p];
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
