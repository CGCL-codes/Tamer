    public BigInteger getPermutationNumber() {
        if (fak == null) {
            fak = new BigInteger[N];
            fak[N - 1] = BigInteger.ONE;
            for (int i = N - 2; i >= 0; i--) fak[i] = fak[i + 1].multiply(new BigInteger(N - i - 1 + ""));
        }
        int[] h = new int[N];
        for (int i = 0; i < N; i++) h[i] = i;
        BigInteger PermNumber = BigInteger.ZERO;
        for (int i = 0; i < N - 1; i++) {
            int j = 0;
            int m = Al[i];
            while (h[i + j] != m) j++;
            PermNumber = PermNumber.add(fak[i].multiply(new BigInteger(j + "")));
            for (; j > 0; j--) h[i + j] = h[i + j - 1];
        }
        return PermNumber;
    }
