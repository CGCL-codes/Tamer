    public int sign(byte[] sigBuf, int sigOff, int sigLen) throws SignatureException {
        if (k == null || !(k instanceof RSAPrivateKey)) {
            throw new SignatureException("Illegal State");
        }
        if (sigLen < k.getModulusLen()) {
            throw new SignatureException("Buffer too short");
        }
        byte[] data = new byte[prefix.length + md.getDigestLength()];
        System.arraycopy(prefix, 0, data, 0, prefix.length);
        try {
            md.digest(data, prefix.length, md.getDigestLength());
            return c.doFinal(data, 0, data.length, sigBuf, sigOff);
        } catch (GeneralSecurityException ce) {
            throw new SignatureException(ce.getMessage());
        }
    }
