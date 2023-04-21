    protected int engineDigest(byte[] buf, int offset, int len) throws DigestException {
        checkState();
        int n = digests[0].digest(buf, offset, len);
        digestReset();
        return n;
    }
