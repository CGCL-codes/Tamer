    public EncryptedProperties(File file, byte[] password) throws AuthenticationException, IOException, ConfigurationException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidKeySpecException, IllegalBlockSizeException, BadPaddingException {
        this.file = file;
        cipher = Cipher.getInstance(CONTENT_ENCRYPT_METHOD);
        spec = new PBEParameterSpec(salt, iterationCount);
        MessageDigest digest = MessageDigest.getInstance(KEY_ENCRYPT_METHOD);
        String encrypted = new String(digest.digest(password)).replace(',', ' ');
        CharArrayWriter writer = new CharArrayWriter(password.length);
        for (byte curr : password) {
            writer.append((char) curr);
        }
        KeySpec keySpec = new PBEKeySpec(writer.toCharArray(), salt, iterationCount);
        key = SecretKeyFactory.getInstance(CONTENT_ENCRYPT_METHOD).generateSecret(keySpec);
        if (!file.exists()) {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.createNewFile()) {
                throw new IOException("File cannot be created");
            }
            properties = new PropertiesConfiguration(file);
            properties.setProperty(PROPERTY_KEY, encrypted);
            properties.setProperty(RESET_KEY, RESET_RESTORE_VALUE);
            properties.save();
        } else {
            properties = new PropertiesConfiguration(file);
            String existing = properties.getString(PROPERTY_KEY);
            if (!existing.equals(encrypted)) {
                throw new AuthenticationException("Invalid password");
            }
            String reset = properties.getString(RESET_KEY);
            if (reset != null && reset.equals(RESET_RESET_VALUE)) {
                decryptAll();
                properties.setProperty(RESET_KEY, RESET_RESTORE_VALUE);
                properties.save();
                throw new AuthenticationException("Properties file has been decrypted. " + "It now can be modified. Next time the file is loaded it will be re-encrypted");
            } else if (reset != null && reset.equals(RESET_RESTORE_VALUE)) {
                encryptAll();
                properties.clearProperty(RESET_KEY);
                properties.save();
            }
        }
    }
