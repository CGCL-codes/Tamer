    public byte[] generateM2(final BigInteger A, final byte[] M1, final byte[] K, final String U, final String I, final String o, final byte[] sid, final int ttl, final byte[] cIV, final byte[] sIV, final byte[] sCB) throws UnsupportedEncodingException {
        final IMessageDigest hash = (IMessageDigest) mda.clone();
        byte[] b;
        b = Util.trim(A);
        hash.update(b, 0, b.length);
        hash.update(M1, 0, M1.length);
        hash.update(K, 0, K.length);
        b = digest(U);
        hash.update(b, 0, b.length);
        b = digest(I);
        hash.update(b, 0, b.length);
        b = digest(o);
        hash.update(b, 0, b.length);
        hash.update(sid, 0, sid.length);
        hash.update((byte) (ttl >>> 24));
        hash.update((byte) (ttl >>> 16));
        hash.update((byte) (ttl >>> 8));
        hash.update((byte) ttl);
        hash.update(cIV, 0, cIV.length);
        hash.update(sIV, 0, sIV.length);
        hash.update(sCB, 0, sCB.length);
        return hash.digest();
    }
