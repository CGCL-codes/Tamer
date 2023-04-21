    private byte[] generateKey(int knownValue) {
        byte[] K = SshMisc.createMpInt(sharedSecretK.getEncoded());
        byte[] dataToHash = new byte[K.length + (2 * H.length) + 1];
        int offset = 0;
        for (int i = 0; i < K.length; i++) {
            dataToHash[offset++] = K[i];
        }
        for (int i = 0; i < H.length; i++) {
            dataToHash[offset++] = H[i];
        }
        dataToHash[offset++] = (byte) knownValue;
        for (int i = 0; i < H.length; i++) {
            dataToHash[offset++] = H[i];
        }
        hasher.update(dataToHash, 0, dataToHash.length);
        byte[] key = hasher.digest();
        byte[] keyToReturn = new byte[16];
        for (int i = 0; i < 16; i++) {
            keyToReturn[i] = key[i];
        }
        return keyToReturn;
    }
