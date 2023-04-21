    public static final byte[] digestBytes(final byte[] data) throws RuntimeException {
        return MD5.get().digest(data);
    }
