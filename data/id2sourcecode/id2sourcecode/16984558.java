    private byte[] getKeyFromPassword(char[] password, byte[] salt, int keyLength) throws Exception {
        MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
        byte[] primitiveKey = new byte[password.length * 2 + salt.length];
        for (int i = 0; i < password.length; i++) {
            primitiveKey[i * 2] = (byte) (password[i] >> 8);
            primitiveKey[i * 2 + 1] = (byte) (password[i] & 0xFF);
        }
        System.arraycopy(salt, 0, primitiveKey, password.length * 2, salt.length);
        md.update(primitiveKey);
        byte[] key = new byte[keyLength];
        System.arraycopy(md.digest(), 0, key, 0, key.length);
        return key;
    }
