    public PasswordEncryptedObject(Object object, char[] password) throws GeneralSecurityException, IOException {
        PBEParameterSpec paramSpec = new PBEParameterSpec(salt, iterations);
        PBEKeySpec keySpec = new PBEKeySpec(password);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
        SecretKey key = keyFactory.generateSecret(keySpec);
        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        this.encrypted = cipher.doFinal(baos.toByteArray());
    }
