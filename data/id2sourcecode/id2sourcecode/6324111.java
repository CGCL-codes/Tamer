    protected byte[] engineSign() throws SignatureException {
        return sign(digest.digest());
    }
