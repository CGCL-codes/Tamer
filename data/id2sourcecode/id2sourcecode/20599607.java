    public EncryptedStoreKeys(String password, byte[] salt) throws CryptoException {
        byte[] key = null;
        try {
            key = password.getBytes("UTF-16LE");
        } catch (Exception e) {
            throw new CryptoException("Error getting bytes for password", e);
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new CryptoException("Error generating SHA256 Digest", e);
        }
        byte[] derivedKey = generateDerivedKey(digest, key, salt, 1000);
        digest.reset();
        byte[] encKeyBytes = new byte[encKeyEntropy.length + derivedKey.length];
        System.arraycopy(encKeyEntropy, 0, encKeyBytes, 0, encKeyEntropy.length);
        System.arraycopy(derivedKey, 0, encKeyBytes, encKeyEntropy.length, derivedKey.length);
        digest.update(encKeyBytes);
        encryptionKey = digest.digest();
        digest.reset();
        byte[] integrityKeyBytes = new byte[integrityKeyEntropy.length + derivedKey.length];
        System.arraycopy(integrityKeyEntropy, 0, integrityKeyBytes, 0, integrityKeyEntropy.length);
        System.arraycopy(derivedKey, 0, integrityKeyBytes, integrityKeyEntropy.length, derivedKey.length);
        digest.update(integrityKeyBytes);
        integrityKey = digest.digest();
    }
