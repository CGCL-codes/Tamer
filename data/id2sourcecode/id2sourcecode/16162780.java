    public CryptoFileIO(String fileName, String access) throws FileNotFoundException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException {
        _raf = new RandomAccessFile(fileName, access);
        String pwd = "password";
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] key = md5.digest(pwd.getBytes());
        SecretKeySpec skey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(md5.digest(key));
        encipher = Cipher.getInstance("AES/CTR/NoPadding");
        encipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
        decipher = Cipher.getInstance("AES/CTR/NoPadding");
        decipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
    }
