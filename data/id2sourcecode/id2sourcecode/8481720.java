    public static KeyPair generateKeyPair(int bits, BigInteger e, SecureRandom secRand) {
        BigInteger p = null;
        BigInteger q = null;
        BigInteger t = null;
        BigInteger phi = null;
        BigInteger d = null;
        BigInteger u = null;
        BigInteger n = null;
        boolean finished = false;
        int pbits = (bits + 1) / 2;
        int qbits = bits - pbits;
        while (!finished) {
            p = new BigInteger(pbits, 80, secRand);
            q = new BigInteger(qbits, 80, secRand);
            if (p.compareTo(q) == 0) {
                continue;
            } else if (p.compareTo(q) < 0) {
                t = q;
                q = p;
                p = t;
            }
            t = p.gcd(q);
            if (t.compareTo(one) != 0) {
                continue;
            }
            n = p.multiply(q);
            if (n.bitLength() != bits) {
                continue;
            }
            phi = p.subtract(one).multiply(q.subtract(one));
            d = e.modInverse(phi);
            u = q.modInverse(p);
            finished = true;
        }
        RSAPrivateCrtKey prvKey = new RSAPrivateCrtKey(n, e, d, p, q, u);
        RSAPublicKey pubKey = new RSAPublicKey(n, e);
        return new KeyPair(pubKey, prvKey);
    }
