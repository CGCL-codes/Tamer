    private static byte[] deriveKey(char[] password, byte[] globalSalt, byte[] entrySalt) throws InvalidKeyException, NoSuchAlgorithmException, ShortBufferException {
        Mac hmac = Mac.getInstance("HmacSHA1");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        byte[] key = new byte[40];
        byte[] pwd = new byte[password.length];
        for (int i = 0; i < pwd.length; i++) {
            pwd[i] = (byte) password[i];
        }
        sha1.update(globalSalt);
        sha1.update(pwd);
        byte[] hp = sha1.digest();
        byte[] pes = new byte[20];
        System.arraycopy(entrySalt, 0, pes, 0, entrySalt.length);
        sha1.update(hp);
        sha1.update(entrySalt);
        byte[] chp = sha1.digest();
        hmac.init(new SecretKeySpec(chp, hmac.getAlgorithm()));
        hmac.update(pes);
        hmac.update(entrySalt);
        hmac.doFinal(key, 0);
        hmac.update(pes);
        byte[] tk = hmac.doFinal();
        hmac.update(tk);
        hmac.update(entrySalt);
        hmac.doFinal(key, 20);
        return key;
    }
