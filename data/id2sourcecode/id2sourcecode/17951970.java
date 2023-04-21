    public static byte[] keyIDToNameComponent(PublisherPublicKeyDigest keyToName) {
        if (null == keyToName) {
            throw new IllegalArgumentException("keyToName must not be null!");
        }
        return keyIDToNameComponent(keyToName.digest());
    }
