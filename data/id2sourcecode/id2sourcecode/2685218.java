    byte[] createKey(byte[] hashedPassword, byte[] nonce) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(hashedPassword, 0, HASH_SIZE);
        md.update(nonce, 0, NONCE_SIZE);
        byte[] hash = md.digest();
        byte[] retval = new byte[16];
        for (int i = 0; i < 16; i++) {
            retval[i] = hash[i];
        }
        return retval;
    }
