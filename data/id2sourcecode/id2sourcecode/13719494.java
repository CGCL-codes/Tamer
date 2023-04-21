    private byte[] deriveKey(final MessageDigest md, final char id, final int len, final BigInteger k, final byte[] h) throws GeneralSecurityException {
        md.reset();
        updateBigInt(md, k);
        md.update(h);
        md.update((byte) id);
        md.update(this.sessionId);
        byte[] a = md.digest();
        final byte[] key = new byte[len];
        System.arraycopy(a, 0, key, 0, Math.min(len, a.length));
        for (int pos = a.length; pos < len; ) {
            a = null;
            updateBigInt(md, k);
            md.update(h);
            md.update(key, 0, pos);
            a = md.digest();
            System.arraycopy(a, 0, key, pos, Math.min(len - pos, a.length));
            pos += a.length;
        }
        return key;
    }
