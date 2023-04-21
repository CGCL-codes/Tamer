    private int getLength(int p) {
        byte[] b = { 0, 0, 0, 0 };
        if (p + super.lengthSize > store.length) {
            return 0;
        }
        b[4 - super.lengthSize] = store[p];
        if (super.lengthSize == 2) {
            b[3] = store[p + 1];
        }
        if (super.lengthSize > 2) {
            b[2] = store[p + 1];
            b[3] = store[p + 2];
        }
        return (new BigInteger(b)).intValue();
    }
