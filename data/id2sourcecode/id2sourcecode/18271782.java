    public byte[] calculateOwnerPassword(String ownerPassword, String userPassword, boolean isAuthentication) {
        if ("".equals(ownerPassword) && !"".equals(userPassword)) {
            ownerPassword = userPassword;
        }
        byte[] paddedOwnerPassword = padPassword(ownerPassword);
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            logger.log(Level.FINE, "Could not fint MD5 Digest", e);
        }
        paddedOwnerPassword = md5.digest(paddedOwnerPassword);
        if (encryptionDictionary.getRevisionNumber() == 3) {
            for (int i = 0; i < 50; i++) {
                paddedOwnerPassword = md5.digest(paddedOwnerPassword);
            }
        }
        int dataSize = 5;
        if (encryptionDictionary.getRevisionNumber() == 3) {
            dataSize = encryptionDictionary.getKeyLength() / 8;
        }
        byte[] encryptionKey = new byte[dataSize];
        System.arraycopy(paddedOwnerPassword, 0, encryptionKey, 0, dataSize);
        if (isAuthentication) {
            return encryptionKey;
        }
        byte[] paddedUserPassword = padPassword(userPassword);
        byte[] finalData = null;
        try {
            SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
            Cipher rc4 = Cipher.getInstance("RC4");
            rc4.init(Cipher.ENCRYPT_MODE, key);
            finalData = rc4.update(paddedUserPassword);
            if (encryptionDictionary.getRevisionNumber() == 3) {
                byte[] indexedKey = new byte[encryptionKey.length];
                for (int i = 1; i <= 19; i++) {
                    for (int j = 0; j < encryptionKey.length; j++) {
                        indexedKey[j] = (byte) (encryptionKey[j] ^ i);
                    }
                    key = new SecretKeySpec(indexedKey, "RC4");
                    rc4.init(Cipher.ENCRYPT_MODE, key);
                    finalData = rc4.update(finalData);
                }
            }
        } catch (NoSuchAlgorithmException ex) {
            logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
        } catch (NoSuchPaddingException ex) {
            logger.log(Level.FINE, "NoSuchPaddingException.", ex);
        } catch (InvalidKeyException ex) {
            logger.log(Level.FINE, "InvalidKeyException.", ex);
        }
        return finalData;
    }
