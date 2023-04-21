    public static byte[] digest(final String input) {
        byte[] digestBytes = null;
        try {
            final MessageDigest md = MessageDigest.getInstance(CryptoConstants.SHA);
            digestBytes = md.digest(input.getBytes());
        } catch (final Exception e) {
            LoggerFactory.getAsyncLogger().error("An error occured while creating the digest", e);
        }
        return digestBytes;
    }
