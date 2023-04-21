    protected Object generateSignature() throws IllegalStateException {
        int modBits = ((RSAPrivateKey) privateKey).getModulus().bitLength();
        byte[] salt = new byte[sLen];
        this.nextRandomBytes(salt);
        byte[] EM = pss.encode(md.digest(), modBits - 1, salt);
        if (Configuration.DEBUG) log.fine("EM (sign): " + Util.toString(EM));
        BigInteger m = new BigInteger(1, EM);
        BigInteger s = RSA.sign(privateKey, m);
        int k = (modBits + 7) / 8;
        return RSA.I2OSP(s, k);
    }
