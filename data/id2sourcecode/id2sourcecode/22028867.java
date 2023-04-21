    private static byte[] createPasswordDigest(char[] password, byte[] salt) {
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance(DIGEST_HASHING_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Your java environment doesn't support the expected cryptographic hash function: " + DIGEST_HASHING_ALGORITHM, e);
        }
        hash.update(salt);
        ByteBuffer passwordBytes = Charset.forName("UTF-8").encode(CharBuffer.wrap(password));
        hash.update(passwordBytes);
        return hash.digest();
    }
