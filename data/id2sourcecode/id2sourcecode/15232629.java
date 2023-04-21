    static byte[] passwordHashStage2(byte[] hashedPassword, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(salt, 0, 4);
        md.update(hashedPassword, 0, SHA1_HASH_SIZE);
        return md.digest();
    }
