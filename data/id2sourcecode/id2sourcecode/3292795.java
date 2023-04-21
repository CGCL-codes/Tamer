    protected boolean verifySignature(Object sig) throws IllegalStateException {
        if (publicKey == null) throw new IllegalStateException();
        byte[] S = (byte[]) sig;
        int modBits = ((RSAPublicKey) publicKey).getModulus().bitLength();
        int k = (modBits + 7) / 8;
        if (S.length != k) return false;
        BigInteger s = new BigInteger(1, S);
        BigInteger m = null;
        try {
            m = RSA.verify(publicKey, s);
        } catch (IllegalArgumentException x) {
            return false;
        }
        int emBits = modBits - 1;
        int emLen = (emBits + 7) / 8;
        byte[] EM = m.toByteArray();
        if (Configuration.DEBUG) log.fine("EM (verify): " + Util.toString(EM));
        if (EM.length > emLen) return false; else if (EM.length < emLen) {
            byte[] newEM = new byte[emLen];
            System.arraycopy(EM, 0, newEM, emLen - EM.length, EM.length);
            EM = newEM;
        }
        byte[] mHash = md.digest();
        boolean result = false;
        try {
            result = pss.decode(mHash, EM, emBits, sLen);
        } catch (IllegalArgumentException x) {
            result = false;
        }
        return result;
    }
