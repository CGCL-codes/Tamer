    private byte[] calculateUValue(byte[] generalKey, byte[] firstDocIdValue, int revision) throws GeneralSecurityException, EncryptionUnsupportedByProductException {
        if (revision == 2) {
            Cipher rc4 = createRC4Cipher();
            SecretKey key = createRC4Key(generalKey);
            initEncryption(rc4, key);
            return crypt(rc4, PW_PADDING);
        } else if (revision >= 3) {
            MessageDigest md5 = createMD5Digest();
            md5.update(PW_PADDING);
            if (firstDocIdValue != null) {
                md5.update(firstDocIdValue);
            }
            final byte[] hash = md5.digest();
            Cipher rc4 = createRC4Cipher();
            SecretKey key = createRC4Key(generalKey);
            initEncryption(rc4, key);
            final byte[] v = crypt(rc4, hash);
            rc4shuffle(v, generalKey, rc4);
            assert v.length == 16;
            final byte[] entryValue = new byte[32];
            System.arraycopy(v, 0, entryValue, 0, v.length);
            System.arraycopy(v, 0, entryValue, 16, v.length);
            return entryValue;
        } else {
            throw new EncryptionUnsupportedByProductException("Unsupported standard security handler revision " + revision);
        }
    }
