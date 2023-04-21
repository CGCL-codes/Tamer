    public void setPermutationNumber(BigInteger Number) {
        if (fak == null) {
            fak = new BigInteger[N];
            fak[N - 1] = BigInteger.ONE;
            for (int i = N - 2; i >= 0; i--) fak[i] = fak[i + 1].multiply(new BigInteger(N - i - 1 + ""));
        }
        Number = Number.mod(fakultaet(N));
        for (int i = 0; i < N; i++) Al[i] = i;
        if (Number.equals(BigInteger.ZERO)) return;
        for (int i = 0; i < N - 1; i++) {
            BigInteger[] bi = Number.divideAndRemainder(fak[i]);
            int j = bi[0].intValue();
            if (j != 0) {
                int m = Al[i + j];
                for (; j > 0; j--) Al[i + j] = Al[i + j - 1];
                Al[i] = m;
                Number = bi[1];
            }
        }
    }
