    public byte[] getByts(String data) {
        try {
            digest.update(data.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return digest.digest();
    }
