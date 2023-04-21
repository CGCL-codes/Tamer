    private byte[] computeX(final byte[] s, final byte[] user, final byte[] p) {
        final IMessageDigest hash = (IMessageDigest) mda.clone();
        hash.update(user, 0, user.length);
        hash.update(COLON);
        hash.update(p, 0, p.length);
        final byte[] up = hash.digest();
        hash.update(s, 0, s.length);
        hash.update(up, 0, up.length);
        return hash.digest();
    }
