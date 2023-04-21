    void loop_mul(int mwidth, TBSymbol v[], TBSymbol z[], TBSymbol b_in, BigInteger poly) {
        TBSymbol tmp;
        TBSymbol newv[];
        newv = new TBSymbol[mwidth];
        for (int i = 0; i < mwidth; i = i + 1) {
            tmp = new TBSymOperator(z[i], new TBSymOperator(v[i], b_in, '&'), '^');
            z[i] = tmp;
        }
        for (int i = 0; i < (mwidth - 1); i++) {
            if (poly.testBit(i)) newv[i] = new TBSymOperator(v[0], v[i + 1], '^'); else newv[i] = v[i + 1];
        }
        newv[mwidth - 1] = v[0];
        for (int i = 0; i < mwidth; i++) v[i] = newv[i];
    }
