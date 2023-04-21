    private byte[] generateDerivedKey(MessageDigest digest, byte[] password, byte[] salt, int iterationCount) {
        digest.update(password);
        digest.update(salt);
        byte[] digestBytes = digest.digest();
        for (int i = 1; i < iterationCount; i++) {
            digest.update(digestBytes);
            digestBytes = digest.digest();
        }
        return digestBytes;
    }
