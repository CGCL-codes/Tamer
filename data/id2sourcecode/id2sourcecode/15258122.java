    public static byte[] decrypt(String pass, String str) throws Exception {
        int iterationCount = 19;
        KeySpec keySpec = new PBEKeySpec(pass.toCharArray(), salt, iterationCount);
        SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
        Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(str.getBytes("UTF8"));
        Base64InputStream decoder = new Base64InputStream(byteArrayInputStream);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte array[] = new byte[2048];
        int nbread = 0;
        while ((nbread = decoder.read(array)) > -1) {
            baos.write(array, 0, nbread);
        }
        byte encoded[] = baos.toByteArray();
        byte decoded[] = dcipher.doFinal(encoded);
        return decoded;
    }
