    public int digest(byte buf[], int offset, int len) throws java.security.DigestException {
        return this.algorithm.digest(buf, offset, len);
    }
