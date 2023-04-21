    public String digestAsString(final byte[] data) {
        byte[] digest = super.digest(data);
        return new String(digest);
    }
