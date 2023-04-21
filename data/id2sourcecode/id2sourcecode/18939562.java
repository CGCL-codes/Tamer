    public static String digestString(String x) {
        if (x == null) return null;
        try {
            return Encryptor.digest(x);
        } catch (Exception e) {
            Logger.debug(PublicEncryptionFactory.class, "", e);
            throw new DotRuntimeException("Encryption digest");
        }
    }
