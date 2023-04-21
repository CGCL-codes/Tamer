    private byte[] deriveKey() throws Exception {
        MessageDigest md = MessageDigest.getInstance(HASH_ALG);
        md.update(this.pass);
        md.update(SALT);
        byte[] key = new byte[KEY_LENGTH];
        System.arraycopy(md.digest(), 0, key, 0, KEY_LENGTH);
        return key;
    }
