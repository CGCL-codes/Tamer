    @Override
    public boolean authenticateOwner(byte[] owner) throws COSSecurityException {
        try {
            byte[] preparedOwner = prepareBytes(owner);
            MessageDigest md = MessageDigest.getInstance(DIGEST_ALGORITHM);
            md.update(preparedOwner);
            byte[] key = md.digest();
            int length = 5;
            byte[] encryptionKey = new byte[length];
            System.arraycopy(key, 0, encryptionKey, 0, length);
            Cipher cipher = Cipher.getInstance(KEY_ALGORITHM);
            if (cipher == null) {
                throw new COSSecurityException("RC4 cipher not found");
            }
            SecretKey skeySpec;
            byte[] encrypted = getO();
            skeySpec = new SecretKeySpec(encryptionKey, KEY_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
            encrypted = cipher.doFinal(encrypted);
            if (authenticateUser(encrypted)) {
                setActiveAccessPermissions(AccessPermissionsFull.get());
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new COSSecurityException(e);
        }
    }
