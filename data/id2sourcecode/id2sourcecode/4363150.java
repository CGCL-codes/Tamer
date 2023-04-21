    public static DSASignature generateSignature(byte[] message, DSAPrivateKey pk, SecureRandom rnd) {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger m = new BigInteger(1, sha_message);
        BigInteger k;
        int qBitLength = pk.getQ().bitLength();
        do {
            k = new BigInteger(qBitLength, rnd);
        } while (k.compareTo(pk.getQ()) >= 0);
        BigInteger r = pk.getG().modPow(k, pk.getP()).mod(pk.getQ());
        k = k.modInverse(pk.getQ()).multiply(m.add((pk).getX().multiply(r)));
        BigInteger s = k.mod(pk.getQ());
        return new DSASignature(r, s);
    }
