    protected void shiftRight() {
        for (int i = 0; i < mant.length - 1; i++) {
            mant[i] = mant[i + 1];
        }
        mant[mant.length - 1] = 0;
        exp++;
    }
