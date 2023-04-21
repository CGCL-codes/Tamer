    public byte[] generateKn(final byte[] K, final byte[] cn, final byte[] sn) {
        final IMessageDigest hash = (IMessageDigest) mda.clone();
        hash.update(K, 0, K.length);
        hash.update(cn, 0, cn.length);
        hash.update(sn, 0, sn.length);
        return hash.digest();
    }
