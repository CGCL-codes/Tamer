    @Override
    protected byte[] createOwnerPassword(byte[] owner, byte[] user) throws COSSecurityException {
        try {
            byte[] preparedOwner;
            if (owner == null) {
                preparedOwner = prepareBytes(user);
            } else {
                preparedOwner = prepareBytes(owner);
            }
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(preparedOwner);
            byte[] key = md.digest();
            int length = getEncryption().getLength() / 8;
            for (int i = 0; i < 50; i++) {
                md.update(key, 0, length);
                key = md.digest();
            }
            byte[] encryptionKey = new byte[length];
            System.arraycopy(key, 0, encryptionKey, 0, length);
            SecretKey skeySpec = new SecretKeySpec(encryptionKey, KEY_ALGORITHM);
            byte[] preparedUser = prepareBytes(user);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(preparedUser);
            byte[] tempKey = new byte[encryptionKey.length];
            for (int i = 1; i <= 19; i++) {
                for (int index = 0; index < encryptionKey.length; index++) {
                    tempKey[index] = (byte) (encryptionKey[index] ^ i);
                }
                skeySpec = new SecretKeySpec(tempKey, KEY_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                encrypted = cipher.doFinal(encrypted);
            }
            return encrypted;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
