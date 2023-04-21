    private byte[] getInitialOwnerPasswordKeyBytes(byte[] ownerPassword, int keyBitLength, int revision) throws GeneralSecurityException {
        final MessageDigest md5 = createMD5Digest();
        md5.update(padPassword(ownerPassword));
        final byte[] hash = md5.digest();
        if (revision >= 3) {
            for (int i = 0; i < 50; ++i) {
                md5.update(hash);
                digestTo(md5, hash);
            }
        }
        final byte[] rc4KeyBytes = new byte[keyBitLength / 8];
        System.arraycopy(hash, 0, rc4KeyBytes, 0, rc4KeyBytes.length);
        return rc4KeyBytes;
    }
