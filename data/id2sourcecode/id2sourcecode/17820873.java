    @Override
    public byte[] digest(final byte[] data) {
        byte[] digested = super.digest(data);
        byte[] hex = HexUtils.toHex(digested);
        return hex;
    }
