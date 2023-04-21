    protected Object generateSignature() throws IllegalStateException {
        final int modBits = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        final int k = (modBits + 7) / 8;
        final byte[] EM = pkcs1.encode(md.digest(), k);
        final BigInteger m = new BigInteger(1, EM);
        final BigInteger s = RSA.sign(privateKey, m);
        return RSA.I2OSP(s, k);
    }
