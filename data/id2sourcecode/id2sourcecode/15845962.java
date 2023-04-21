    public byte[] generateServerEvidence(BigInteger A, byte[] M, byte[] K, String U, String I, String o) {
        MessageDigest hash = newDigest();
        byte[] aBytes = SaslUtil.trim(A);
        hash.update(aBytes);
        hash.update(M);
        hash.update(K);
        hash.update(digest(U));
        hash.update(digest(I));
        hash.update(digest(o));
        return hash.digest();
    }
