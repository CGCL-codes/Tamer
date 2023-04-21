    private boolean generateKey() {
        try {
            if (secretKeyFile.exists() && !ui.confirm("The secret key '" + secretKeyFile + "' already exists. Overwrite it?")) {
                return false;
            }
            FileUtils.forceMkdir(keyStorageDir);
            char[] password = ui.createPassword();
            if (password == null) {
                return false;
            }
            try {
                PBEKeySpec passwordKeySpec = new PBEKeySpec(password);
                SecretKeyFactory factory = SecretKeyFactory.getInstance(KEY_ENCRYPTION_ALGORITHM);
                SecretKey passwordKey = factory.generateSecret(passwordKeySpec);
                Cipher cipher = Cipher.getInstance(KEY_ENCRYPTION_ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, passwordKey);
                KeyGenerator generator = KeyGenerator.getInstance(DATA_ENCRYPTION_ALGORITHM);
                secretKey = generator.generateKey();
                writeSecretKey(cipher);
            } finally {
                Arrays.fill(password, '\0');
            }
            ui.information(Strings.getSecretKeyGeneratedMessage(keyStorageDir.getAbsolutePath()));
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }
