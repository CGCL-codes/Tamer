    public String createBase64Digest(DigestContext context) {
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(context.getDigest().digest());
    }
