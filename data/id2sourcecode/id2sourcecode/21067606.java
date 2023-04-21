    @Override
    public boolean authenticateOwner(byte[] owner) throws COSSecurityException {
        try {
            byte[] preparedOwner = prepareBytes(owner);
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(preparedOwner);
            byte[] key = md.digest();
            for (int i = 0; i < 50; i++) {
                md.update(key);
                key = md.digest();
            }
            int length = 16;
            byte[] encryptionKey = new byte[length];
            System.arraycopy(key, 0, encryptionKey, 0, length);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            if (cipher == null) {
                throw new COSSecurityException("RC4 cipher not found");
            }
            SecretKey skeySpec;
            byte[] encrypted = getO();
            byte[] tempEncKey = new byte[encryptionKey.length];
            for (int i = 19; i >= 0; i--) {
                for (int index = 0; index < encryptionKey.length; index++) {
                    tempEncKey[index] = (byte) (encryptionKey[index] ^ i);
                }
                skeySpec = new SecretKeySpec(tempEncKey, KEY_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                encrypted = cipher.doFinal(encrypted);
            }
            if (authenticateUser(encrypted)) {
                setActiveAccessPermissions(AccessPermissionsFull.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
