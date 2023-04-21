    private Cipher generateCipher(String pass, int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        byte[] key = MessageDigest.getInstance("MD5").digest(pass.getBytes());
        SecretKeySpec secretkey = new SecretKeySpec(key, CRYPTO_ALGORITHM);
        Cipher ci = Cipher.getInstance(CRYPTO_ALGORITHM);
        ci.init(mode, secretkey);
        return ci;
    }
