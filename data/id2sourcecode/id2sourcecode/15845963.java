    public byte[] generateClientEvidence(BigInteger N, BigInteger g, String U, byte[] s, BigInteger A, BigInteger B, byte[] K, String L) {
        MessageDigest hash = newDigest();
        byte[] nBytes = SaslUtil.trim(N);
        byte[] gBytes = SaslUtil.trim(g);
        byte[] aBytes = SaslUtil.trim(A);
        byte[] bBytes = SaslUtil.trim(B);
        byte[] dnb = digest(nBytes);
        byte[] dgb = digest(gBytes);
        byte[] xb = xor(dnb, dgb);
        hash.update(xb);
        hash.update(digest(U));
        hash.update(s);
        hash.update(aBytes);
        hash.update(bBytes);
        hash.update(K);
        hash.update(digest(L));
        return hash.digest();
    }
