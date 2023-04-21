    BigInteger generateV(BigInteger y, BigInteger p, BigInteger q, BigInteger g, BigInteger w, BigInteger r) {
        byte[] s2 = dataSHA.digest();
        BigInteger temp = new BigInteger(1, s2);
        temp = temp.multiply(w);
        BigInteger u1 = temp.remainder(q);
        BigInteger u2 = (r.multiply(w)).remainder(q);
        BigInteger t1 = g.modPow(u1, p);
        BigInteger t2 = y.modPow(u2, p);
        BigInteger t3 = t1.multiply(t2);
        BigInteger t5 = t3.remainder(p);
        return t5.remainder(q);
    }
