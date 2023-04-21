    public byte[] hash(byte[] data) {
        MessageDigest md = getDigestObject();
        md.update(data);
        return md.digest();
    }
