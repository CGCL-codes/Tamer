    public void init(String fileName, boolean canWrite, String password) throws Exception {
        fileIO = new OdbFileIO(fileName, canWrite, password);
        this.fileName = fileName;
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] key = md5.digest(password.getBytes());
        SecretKeySpec skey = new SecretKeySpec(key, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(md5.digest(key));
        encipher = Cipher.getInstance("AES/CTR/NoPadding");
        encipher.init(Cipher.ENCRYPT_MODE, skey, ivSpec);
        decipher = Cipher.getInstance("AES/CTR/NoPadding");
        decipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);
    }
