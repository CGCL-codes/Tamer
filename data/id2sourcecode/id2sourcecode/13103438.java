    public byte[] calculatesHash(byte[] document, String algorithm) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(algorithm);
            digest.update(document);
        } catch (NoSuchAlgorithmException e) {
            throw new KeyStoreAccessException(Bundle.getInstance().getResourceString(this, "KT_UNSUPPORTED_ALGORITHM_ERROR").replace("[ALGORITHM]", algorithm), e);
        }
        return digest.digest();
    }
