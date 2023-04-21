    public byte[] calculateUserPassword(String userPassword) {
        byte[] encryptionKey = encryptionKeyAlgorithm(userPassword, encryptionDictionary.getKeyLength());
        if (encryptionDictionary.getRevisionNumber() == 2) {
            byte[] paddedUserPassword = PADDING.clone();
            byte[] finalData = null;
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.ENCRYPT_MODE, key);
                finalData = rc4.doFinal(paddedUserPassword);
            } catch (NoSuchAlgorithmException ex) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
            } catch (IllegalBlockSizeException ex) {
                logger.log(Level.FINE, "IllegalBlockSizeException.", ex);
            } catch (BadPaddingException ex) {
                logger.log(Level.FINE, "BadPaddingException.", ex);
            } catch (NoSuchPaddingException ex) {
                logger.log(Level.FINE, "NoSuchPaddingException.", ex);
            } catch (InvalidKeyException ex) {
                logger.log(Level.FINE, "InvalidKeyException.", ex);
            }
            return finalData;
        } else if (encryptionDictionary.getRevisionNumber() == 3) {
            byte[] paddedUserPassword = PADDING.clone();
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                logger.log(Level.FINE, "MD5 digester could not be found", e);
            }
            md5.update(paddedUserPassword);
            String firstFileID = ((StringObject) encryptionDictionary.getFileID().elementAt(0)).getLiteralString();
            byte[] fileID = new byte[firstFileID.length()];
            for (int i = 0; i < firstFileID.length(); i++) {
                fileID[i] = (byte) firstFileID.charAt(i);
            }
            byte[] encryptData = md5.digest(fileID);
            try {
                SecretKeySpec key = new SecretKeySpec(encryptionKey, "RC4");
                Cipher rc4 = Cipher.getInstance("RC4");
                rc4.init(Cipher.ENCRYPT_MODE, key);
                encryptData = rc4.update(encryptData);
                byte[] indexedKey = new byte[encryptionKey.length];
                for (int i = 1; i <= 19; i++) {
                    for (int j = 0; j < encryptionKey.length; j++) {
                        indexedKey[j] = (byte) (encryptionKey[j] ^ (byte) i);
                    }
                    key = new SecretKeySpec(indexedKey, "RC4");
                    rc4.init(Cipher.ENCRYPT_MODE, key);
                    encryptData = rc4.update(encryptData);
                }
            } catch (NoSuchAlgorithmException ex) {
                logger.log(Level.FINE, "NoSuchAlgorithmException.", ex);
            } catch (NoSuchPaddingException ex) {
                logger.log(Level.FINE, "NoSuchPaddingException.", ex);
            } catch (InvalidKeyException ex) {
                logger.log(Level.FINE, "InvalidKeyException.", ex);
            }
            byte[] finalData = new byte[32];
            System.arraycopy(encryptData, 0, finalData, 0, 16);
            System.arraycopy(PADDING, 0, finalData, 16, 16);
            return finalData;
        } else {
            return null;
        }
    }
