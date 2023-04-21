    BigInteger generateS(BigInteger x, BigInteger q, BigInteger r, BigInteger k) {
        byte[] s2 = dataSHA.digest();
        BigInteger temp = new BigInteger(1, s2);
        BigInteger k1 = k.modInverse(q);
        BigInteger s = x.multiply(r);
        s = temp.add(s);
        s = k1.multiply(s);
        return s.remainder(q);
    }
