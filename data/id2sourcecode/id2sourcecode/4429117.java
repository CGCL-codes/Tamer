    @Override
    protected byte[] createUserPassword(byte[] user) throws COSSecurityException {
        try {
            byte[] encryptionKey = createCryptKey(user);
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(PADDING);
            byte[] fd = getPermanentFileID();
            if (fd != null) {
                md.update(fd);
            }
            byte[] hash = md.digest();
            SecretKey skeySpec = new SecretKeySpec(encryptionKey, KEY_ALGORITHM);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(hash);
            byte[] tempEncKey = new byte[encryptionKey.length];
            for (int i = 1; i <= 19; i++) {
                for (int index = 0; index < encryptionKey.length; index++) {
                    tempEncKey[index] = (byte) (encryptionKey[index] ^ i);
                }
                skeySpec = new SecretKeySpec(tempEncKey, KEY_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                encrypted = cipher.doFinal(encrypted);
            }
            byte[] result = new byte[32];
            System.arraycopy(encrypted, 0, result, 0, 16);
            System.arraycopy(USER_R3_PADDING, 0, result, 16, 16);
            return result;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
