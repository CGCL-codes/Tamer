    public byte[] hash(byte[] data, int offset, int length) {
        MessageDigest md = getDigestObject();
        md.update(data, offset, length);
        return md.digest();
    }
