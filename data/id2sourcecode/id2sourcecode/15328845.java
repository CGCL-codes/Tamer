    private byte[] calculateGeneralEncryptionKey(byte[] userPassword, byte[] firstDocIdValue, int keyBitLength, int revision, byte[] oValue, int pValue, boolean encryptMetadata) throws GeneralSecurityException {
        final byte[] paddedPassword = padPassword(userPassword);
        MessageDigest md5 = createMD5Digest();
        md5.reset();
        md5.update(paddedPassword);
        md5.update(oValue);
        md5.update((byte) (pValue & 0xFF));
        md5.update((byte) ((pValue >> 8) & 0xFF));
        md5.update((byte) ((pValue >> 16) & 0xFF));
        md5.update((byte) (pValue >> 24));
        if (firstDocIdValue != null) {
            md5.update(firstDocIdValue);
        }
        if (revision >= 4 && !encryptMetadata) {
            for (int i = 0; i < 4; ++i) {
                md5.update((byte) 0xFF);
            }
        }
        byte[] hash = md5.digest();
        final int keyLen = revision == 2 ? 5 : (keyBitLength / 8);
        final byte[] key = new byte[keyLen];
        if (revision >= 3) {
            for (int i = 0; i < 50; ++i) {
                md5.update(hash, 0, key.length);
                digestTo(md5, hash);
            }
        }
        System.arraycopy(hash, 0, key, 0, key.length);
        return key;
    }
