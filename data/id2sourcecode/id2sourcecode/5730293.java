    public static byte[] wrappingKeyIdentifier(Key wrappingKey) {
        return CCNDigestHelper.digest(wrappingKey.getEncoded());
    }
