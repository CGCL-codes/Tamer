    public byte[] manifestDigest(MessageDigest md) {
        md.reset();
        md.update(rawBytes, 0, rawBytes.length);
        return md.digest();
    }
