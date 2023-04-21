    public void loadKey(String publicKeyPath, String privateKeyPath) {
        if (publicKey != null || privateKey != null) {
            return;
        } else {
            publicKey = loadKey(publicKeyPath);
            privateKey = loadKey(privateKeyPath);
            if (publicKey == null || privateKey == null) {
                generateKeyPair(KEY_SIZE);
                saveKeyToFile(privateKey, privateKeyPath);
                saveKeyToFile(publicKey, publicKeyPath);
            }
        }
    }
