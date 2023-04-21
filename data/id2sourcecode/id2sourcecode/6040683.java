    protected boolean verifySignature(Object sig) throws IllegalStateException {
        final BigInteger[] rs = decodeSignature(sig);
        return checkRS(rs, md.digest());
    }
