    public byte[] digest(final byte[] src) {
        final IMessageDigest hash = (IMessageDigest) mda.clone();
        hash.update(src, 0, src.length);
        return hash.digest();
    }
