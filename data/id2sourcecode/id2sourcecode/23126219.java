    public boolean verify(byte[] sigBuf, int sigOff, int sigLen) throws SignatureException {
        if (k == null || !(k instanceof RSAPublicKey)) {
            throw new SignatureException("Illegal State");
        }
        byte[] res = null;
        int val;
        byte[] digest = new byte[md.getDigestLength()];
        try {
            md.digest(digest, 0, digest.length);
            res = new byte[k.getModulusLen()];
            val = c.doFinal(sigBuf, sigOff, sigLen, res, 0);
        } catch (IllegalArgumentException iae) {
            throw new SignatureException(iae.getMessage());
        } catch (GeneralSecurityException e) {
            return false;
        }
        int size = prefix.length + md.getDigestLength();
        if (val != size) {
            return false;
        }
        for (int i = 0; i < prefix.length; i++) {
            if (res[i] != prefix[i]) {
                return false;
            }
        }
        for (int i = prefix.length; i < size; i++) {
            if (res[i] != digest[i - prefix.length]) {
                return false;
            }
        }
        return true;
    }
