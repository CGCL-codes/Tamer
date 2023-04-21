    public int engineDigest(byte[] buf, int offset, int len) throws DigestException {
        int result = adaptee.hashSize();
        if (len < result) throw new DigestException();
        byte[] md = adaptee.digest();
        System.arraycopy(md, 0, buf, offset, result);
        return result;
    }
