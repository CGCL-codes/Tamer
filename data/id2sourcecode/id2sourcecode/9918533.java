    public RSAPrivateKey getPrivateKey(String passphrase_) throws IOException {
        Cipher cipher = Cipher.getInstance("DES3");
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 hash not implemented!");
        }
        byte[] key = md5.digest(passphrase_.getBytes());
        cipher.setKey(key);
        byte[] decrypted = cipher.decrypt(_encrypted);
        return _getPrivateKey(decrypted);
    }
