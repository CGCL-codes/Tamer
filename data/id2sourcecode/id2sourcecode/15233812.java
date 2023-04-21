    @Override
    public byte[] digest(final byte[] data) {
        Assert.notEmpty(data, "data");
        try {
            MessageDigest digest = MessageDigest.getInstance(this.type.getAlgorithm());
            digest.update(data);
            byte[] digested = digest.digest();
            return digested;
        } catch (NoSuchAlgorithmException e) {
            throw new DigesterException(e);
        }
    }
