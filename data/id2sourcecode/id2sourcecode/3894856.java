    private byte[] encryptWithHash(byte[] data, byte[] hash, int size) {
        hash = digest.digest(hash);
        byte[] key = new byte[size];
        for (int i = 0; i < size; i++) {
            key[i] = hash[i];
        }
        return encryptWithKey(data, key);
    }
