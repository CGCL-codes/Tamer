    public static BigInteger encrypt(FHEParams fheparams, GHKeyGen key, int b) {
        int i, num = 1;
        int[] bit = new int[1];
        bit[0] = b;
        long n = 1 << (fheparams.logn);
        double p = ((double) fheparams.noise) / n;
        if (p > 0.5) p = 0.5;
        BigInteger[] vals = evalRandPoly(num, n, p, key.root, key.det);
        BigInteger[] out = new BigInteger[num];
        for (i = 0; i < num; i++) {
            out[i] = vals[i + 1];
        }
        for (i = 0; i < num; i++) {
            out[i] = out[i].shiftLeft(1);
            out[i] = out[i].add(new BigInteger(Integer.toString(bit[i])));
            if (out[i].compareTo(key.det) >= 0) out[i] = out[i].subtract(key.det);
        }
        return out[0];
    }
