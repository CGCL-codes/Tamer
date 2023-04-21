    static byte[] createKeyDigest(String password, byte[] docIdData) {
        check16Bytes(docIdData, "docId");
        int nChars = Math.min(password.length(), 16);
        byte[] passwordData = new byte[nChars * 2];
        for (int i = 0; i < nChars; i++) {
            char ch = password.charAt(i);
            passwordData[i * 2 + 0] = (byte) ((ch << 0) & 0xFF);
            passwordData[i * 2 + 1] = (byte) ((ch << 8) & 0xFF);
        }
        byte[] kd;
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md5.update(passwordData);
        byte[] passwordHash = md5.digest();
        md5.reset();
        for (int i = 0; i < 16; i++) {
            md5.update(passwordHash, 0, PASSWORD_HASH_NUMBER_OF_BYTES_USED);
            md5.update(docIdData, 0, docIdData.length);
        }
        kd = md5.digest();
        byte[] result = new byte[KEY_DIGEST_LENGTH];
        System.arraycopy(kd, 0, result, 0, KEY_DIGEST_LENGTH);
        return result;
    }
