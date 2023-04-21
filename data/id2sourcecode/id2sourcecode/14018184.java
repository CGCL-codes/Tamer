    @Override
    public String generateKey(MessageDigestOutputStream generator) {
        final MessageDigest messageDigest = generator.getMessageDigest();
        final byte[] digest = messageDigest.digest();
        return this.encodeHash(digest);
    }
