    public byte[] generateM1(final BigInteger N, final BigInteger g, final String U, final byte[] s, final BigInteger A, final BigInteger B, final byte[] K, final String I, final String L, final byte[] cn, final byte[] cCB) throws UnsupportedEncodingException {
        final IMessageDigest hash = (IMessageDigest) mda.clone();
        byte[] b;
        b = xor(digest(Util.trim(N)), digest(Util.trim(g)));
        hash.update(b, 0, b.length);
        b = digest(U);
        hash.update(b, 0, b.length);
        hash.update(s, 0, s.length);
        b = Util.trim(A);
        hash.update(b, 0, b.length);
        b = Util.trim(B);
        hash.update(b, 0, b.length);
        hash.update(K, 0, K.length);
        b = digest(I);
        hash.update(b, 0, b.length);
        b = digest(L);
        hash.update(b, 0, b.length);
        hash.update(cn, 0, cn.length);
        hash.update(cCB, 0, cCB.length);
        return hash.digest();
    }
