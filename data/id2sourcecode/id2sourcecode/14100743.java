    private void writeDER(char[] password, PrivateKey privateKey, File file) throws IOException, GeneralSecurityException {
        byte[] bytes = null;
        if (password == null) {
            bytes = privateKey.getEncoded();
        } else {
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            PBEKeySpec passwordSpec = new PBEKeySpec(password);
            SecretKey secretKey = secretKeyFactory.generateSecret(passwordSpec);
            Cipher cipher = Cipher.getInstance(secretKey.getAlgorithm());
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedPrivateKey = cipher.doFinal(privateKey.getEncoded());
            EncryptedPrivateKeyInfo encryptedPrivateKeyInfo = new EncryptedPrivateKeyInfo(cipher.getParameters(), encryptedPrivateKey);
            bytes = encryptedPrivateKeyInfo.getEncoded();
        }
        FileUtils.writeByteArrayToFile(file, bytes);
    }
