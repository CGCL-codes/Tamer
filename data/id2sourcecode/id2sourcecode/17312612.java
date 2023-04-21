    private static byte[] getKeyFromClassInfo(byte[] classInfo) {
        byte[] keyBytes = new byte[1 + SHA1_SIZE];
        keyBytes[0] = DataStoreHeader.CLASS_HASH_PREFIX;
        MessageDigest md = messageDigest.get();
        try {
            md.update(classInfo);
            int numBytes = md.digest(keyBytes, 1, SHA1_SIZE);
            assert numBytes == SHA1_SIZE;
            return keyBytes;
        } catch (DigestException e) {
            throw new AssertionError(e);
        }
    }
