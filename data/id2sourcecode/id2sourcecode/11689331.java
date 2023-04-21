    public synchronized void createKey(char[] password) throws Exception {
        if (DEBUG) {
            System.out.println("Generating a Rijndael key...");
        }
        KeyGenerator keyGenerator = KeyGenerator.getInstance("Rijndael");
        keyGenerator.init(128);
        Key key = keyGenerator.generateKey();
        if (DEBUG) {
            System.out.println("Done generating the key.");
        }
        byte[] salt = new byte[8];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        PBEKeySpec pbeKeySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
        SecretKey pbeKey = keyFactory.generateSecret(pbeKeySpec);
        PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, ITERATIONS);
        Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES");
        cipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
        byte[] encryptedKeyBytes = cipher.doFinal(key.getEncoded());
        String keyfile = KEY_FILENAME;
        if (KEY_PATH.trim().length() == 0) {
        } else {
            if (KEY_PATH.endsWith(File.separator)) {
                keyfile = KEY_PATH + KEY_FILENAME;
            } else {
                keyfile = KEY_PATH + File.separator + KEY_FILENAME;
            }
        }
        FileOutputStream fos = new FileOutputStream(keyfile);
        fos.write(salt);
        fos.write(encryptedKeyBytes);
        fos.close();
        BASE64Encoder encoder = new BASE64Encoder();
        String myString = encoder.encode(encryptedKeyBytes);
        if (DEBUG) {
            System.out.println(myString);
        }
    }
