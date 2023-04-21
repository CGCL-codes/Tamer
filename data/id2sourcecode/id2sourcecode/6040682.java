    protected Object generateSignature() throws IllegalStateException {
        final BigInteger[] rs = computeRS(md.digest());
        return encodeSignature(rs[0], rs[1]);
    }
