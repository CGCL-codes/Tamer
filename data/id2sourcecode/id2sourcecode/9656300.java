    private static Cipher createCipher(String passphrase, int encMode) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchPaddingException, UnsupportedEncodingException, NoSuchProviderException {
        byte[] hashedPass = null;
        hashedPass = sha384.digest(passphrase.getBytes("US-ASCII"));
        SecretKeySpec key = new SecretKeySpec(hashedPass, 0, 32, "AES");
        IvParameterSpec Iv = new IvParameterSpec(hashedPass, 32, 16);
        Cipher cipher = null;
        if (cryptoProvider != null) cipher = Cipher.getInstance("AES/OFB/NoPadding", cryptoProvider); else cipher = Cipher.getInstance("AES/OFB/NoPadding");
        cipher.init(encMode, key, Iv);
        return cipher;
    }
