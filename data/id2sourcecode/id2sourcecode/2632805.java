    protected byte[] generateAESKey1(byte[] iv, byte[] password) {
        byte[] aesKey = new byte[KEY_SIZE];
        System.arraycopy(iv, 0, aesKey, 0, iv.length);
        for (int i = 0; i < 8192; i++) {
            digest.reset();
            digest.update(aesKey);
            digest.update(password);
            aesKey = digest.digest();
        }
        return aesKey;
    }
