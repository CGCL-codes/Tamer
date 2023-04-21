    private int next() {
        long val = 0;
        for (int i = 0; i < 4; ++i) val += a[i] * x[i];
        val += carry;
        carry = val >> shiftRight;
        for (int i = 0; i < 3; ++i) x[i] = x[i + 1];
        x[3] = val & b;
        return (int) x[3];
    }
