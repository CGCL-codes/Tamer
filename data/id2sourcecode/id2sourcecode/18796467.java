    public KeyPair generateKeyPair() {
        int halfStrength = (strength_ + 1) / 2;
        BigInteger p = new BigInteger(halfStrength, CERTAINTY, secureRandom_);
        BigInteger q;
        do {
            q = new BigInteger(halfStrength, CERTAINTY, secureRandom_);
        } while (p.equals(q));
        BigInteger n = p.multiply(q);
        BigInteger one = BigInteger.ONE;
        BigInteger two = BigInteger.valueOf(2);
        BigInteger pm = p.subtract(one);
        BigInteger qm = q.subtract(one);
        BigInteger phi = pm.multiply(qm);
        if (e_ == null) {
            e_ = BigInteger.valueOf(DEFAULT_EXPONENT);
            while (!(phi.gcd(e_)).equals(one)) {
                e_ = e_.add(two);
            }
        }
        BigInteger d = e_.modInverse(phi);
        BigInteger exponentOne = d.mod(pm);
        BigInteger exponentTwo = d.mod(qm);
        BigInteger crt = q.modInverse(p);
        RSAPubKey pub = new RSAPubKey(n, e_);
        RSAPrivCrtKey priv = new RSAPrivCrtKey(n, d, e_, p, q, exponentOne, exponentTwo, crt);
        return (new KeyPair(pub, priv));
    }
