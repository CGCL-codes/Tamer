    private void createKeysIfNecessary() throws IOException {
        File publicKeyFile = new File(HOST_KEY_NAME + ".pub");
        File privateKeyFile = new File(HOST_KEY_NAME);
        if (!(publicKeyFile.exists() && privateKeyFile.exists())) {
            publicKeyFile.delete();
            privateKeyFile.delete();
            SshKeyGenerator generator = new SshKeyGenerator();
            generator.generateKeyPair("DSA", 1024, HOST_KEY_NAME, System.getProperty("user.name"), DEFAULT_KEY_PASSPHRASE);
        }
    }
