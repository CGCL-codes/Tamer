    public KeyPair generate() {
        if (Configuration.DEBUG) log.entering(this.getClass().getName(), "generate");
        BigInteger p, q, n, d;
        int M = (L + 1) / 2;
        BigInteger lower = TWO.pow(M - 1);
        BigInteger upper = TWO.pow(M).subtract(ONE);
        byte[] kb = new byte[(M + 7) / 8];
        step1: while (true) {
            nextRandomBytes(kb);
            p = new BigInteger(1, kb).setBit(0);
            if (p.compareTo(lower) >= 0 && p.compareTo(upper) <= 0 && p.isProbablePrime(80) && p.gcd(e).equals(ONE)) break step1;
        }
        step2: while (true) {
            nextRandomBytes(kb);
            q = new BigInteger(1, kb).setBit(0);
            n = p.multiply(q);
            if (n.bitLength() == L && q.isProbablePrime(80) && q.gcd(e).equals(ONE)) break step2;
        }
        BigInteger phi = p.subtract(ONE).multiply(q.subtract(ONE));
        d = e.modInverse(phi);
        PublicKey pubK = new GnuRSAPublicKey(preferredFormat, n, e);
        PrivateKey secK = new GnuRSAPrivateKey(preferredFormat, p, q, e, d);
        KeyPair result = new KeyPair(pubK, secK);
        if (Configuration.DEBUG) log.exiting(this.getClass().getName(), "generate", result);
        return result;
    }
