    protected boolean engineVerify(byte[] sigBytes) throws SignatureException {
        return verify(sigBytes, digest.digest());
    }
