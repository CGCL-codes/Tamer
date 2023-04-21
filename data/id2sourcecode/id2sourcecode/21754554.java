    protected BigInteger uValue(final BigInteger A, final BigInteger B) {
        final IMessageDigest hash = srp.newDigest();
        byte[] b;
        b = Util.trim(A);
        hash.update(b, 0, b.length);
        b = Util.trim(B);
        hash.update(b, 0, b.length);
        return new BigInteger(1, hash.digest());
    }
