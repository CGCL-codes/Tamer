    private static String crypt(String toBeEncrypted, String additive, int cryptMode) throws Exception {
        String returnVal = null;
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        byte[] key = PEPPER.getBytes(ENCODING);
        byte[] additiveBytes = additive.getBytes(ENCODING);
        key = sha.digest(key);
        additiveBytes = sha.digest(additiveBytes);
        byte[] sessionKey = Arrays.copyOf(key, 16);
        byte[] iv = Arrays.copyOf(additiveBytes, 16);
        byte[] plaintext = toBeEncrypted.getBytes(ENCODING);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(cryptMode, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
        byte[] resultBytes = cipher.doFinal(plaintext);
        returnVal = new String(resultBytes, ENCODING);
        return returnVal;
    }
