    protected void shiftRight() {
        for (int i = 0; i < DIGITS - 1; i++) mant[i] = mant[i + 1];
        mant[DIGITS - 1] = 0;
        exp++;
    }
