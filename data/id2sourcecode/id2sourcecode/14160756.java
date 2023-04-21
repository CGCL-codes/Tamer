    @Override
    public CraiKeyPair generateRSAKeyPair(int strength) {
        BigInteger p, q, n, d, e, pSub1, qSub1, phi;
        int pbitlength = (strength + 1) / 2;
        int qbitlength = strength - pbitlength;
        int mindiffbits = strength / 3;
        e = BigInteger.valueOf(0x10001);
        for (; ; ) {
            p = new BigInteger(pbitlength, 1, (Random) mCraiRandom);
            if (p.mod(e).equals(BigInteger.ONE)) {
                continue;
            }
            if (!p.isProbablePrime(1024)) {
                continue;
            }
            if (e.gcd(p.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                break;
            }
        }
        for (; ; ) {
            for (; ; ) {
                q = new BigInteger(qbitlength, 1, (Random) mCraiRandom);
                if (q.subtract(p).abs().bitLength() < mindiffbits) {
                    continue;
                }
                if (q.mod(e).equals(BigInteger.ONE)) {
                    continue;
                }
                if (!q.isProbablePrime(1024)) {
                    continue;
                }
                if (e.gcd(q.subtract(BigInteger.ONE)).equals(BigInteger.ONE)) {
                    break;
                }
            }
            n = p.multiply(q);
            if (n.bitLength() == strength) {
                break;
            }
            p = p.max(q);
        }
        if (p.compareTo(q) < 0) {
            phi = p;
            p = q;
            q = phi;
        }
        pSub1 = p.subtract(BigInteger.ONE);
        qSub1 = q.subtract(BigInteger.ONE);
        phi = pSub1.multiply(qSub1);
        d = e.modInverse(phi);
        BigInteger dP, dQ, qInv;
        dP = d.remainder(pSub1);
        dQ = d.remainder(qSub1);
        qInv = q.modInverse(p);
        return new CraiKeyPair(new OurPublicRSAKey(n, e), new OurPrivateRSAKey(n, d, p, q));
    }
