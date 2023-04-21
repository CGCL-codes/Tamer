    public AsymmetricCipherKeyPair generateKeyPair() {
        BigInteger p, q, n, d, e, pSub1, qSub1, phi;
        int strength = param.getStrength();
        int pbitlength = (strength + 1) / 2;
        int qbitlength = strength - pbitlength;
        int mindiffbits = strength / 3;
        e = param.getPublicExponent();
        for (; ; ) {
            p = new BigInteger(pbitlength, 1, param.getRandom());
            if (p.mod(e).equals(ONE)) {
                continue;
            }
            if (!p.isProbablePrime(param.getCertainty())) {
                continue;
            }
            if (e.gcd(p.subtract(ONE)).equals(ONE)) {
                break;
            }
        }
        for (; ; ) {
            for (; ; ) {
                q = new BigInteger(qbitlength, 1, param.getRandom());
                if (q.subtract(p).abs().bitLength() < mindiffbits) {
                    continue;
                }
                if (q.mod(e).equals(ONE)) {
                    continue;
                }
                if (!q.isProbablePrime(param.getCertainty())) {
                    continue;
                }
                if (e.gcd(q.subtract(ONE)).equals(ONE)) {
                    break;
                }
            }
            n = p.multiply(q);
            if (n.bitLength() == param.getStrength()) {
                break;
            }
            p = p.max(q);
        }
        if (p.compareTo(q) < 0) {
            phi = p;
            p = q;
            q = phi;
        }
        pSub1 = p.subtract(ONE);
        qSub1 = q.subtract(ONE);
        phi = pSub1.multiply(qSub1);
        d = e.modInverse(phi);
        BigInteger dP, dQ, qInv;
        dP = d.remainder(pSub1);
        dQ = d.remainder(qSub1);
        qInv = q.modInverse(p);
        return new AsymmetricCipherKeyPair(new RSAKeyParameters(false, n, e), new RSAPrivateCrtKeyParameters(n, e, d, p, q, dP, dQ, qInv));
    }
