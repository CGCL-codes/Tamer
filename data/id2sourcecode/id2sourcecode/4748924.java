    public String getDigestForString(String string) {
        byte[] bytes = fMessageDigest.digest(string.getBytes());
        return fEncoder.encode(bytes);
    }
