    @Override
    public byte[] digest(final byte[] data) {
        byte[] digested = super.digest(data);
        byte[] base64 = Base64Utils.toBase64(digested);
        return base64;
    }
