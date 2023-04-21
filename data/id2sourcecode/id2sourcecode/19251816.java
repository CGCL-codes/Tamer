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
            int length = 5;
            byte[] encryptionKey = new byte[length];
            System.arraycopy(key, 0, encryptionKey, 0, length);
            SecretKey skeySpec = new SecretKeySpec(encryptionKey, KEY_ALGORITHM);
            byte[] preparedUser = prepareBytes(user);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(preparedUser);
            return encrypted;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
