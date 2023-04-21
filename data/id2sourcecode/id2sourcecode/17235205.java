    protected boolean verifySignature(final Object sig) throws IllegalStateException {
        if (publicKey == null) throw new IllegalStateException();
        final byte[] S = (byte[]) sig;
        final int modBits = ((RSAPublicKey) publicKey).getModulus().bitLength();
        final int k = (modBits + 7) / 8;
        if (S.length != k) return false;
        final BigInteger s = new BigInteger(1, S);
        final BigInteger m;
        try {
            m = RSA.verify(publicKey, s);
        } catch (IllegalArgumentException x) {
            return false;
        }
        final byte[] EM;
        try {
            EM = RSA.I2OSP(m, k);
        } catch (IllegalArgumentException x) {
            return false;
        }
        final byte[] EMp = pkcs1.encode(md.digest(), k);
        return Arrays.equals(EM, EMp);
    }
