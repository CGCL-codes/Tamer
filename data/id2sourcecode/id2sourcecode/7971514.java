    @Override
    public byte[] stretchKey(byte[] pw, byte[] salt, int iterations) {
        byte[] stretchedKey = saltKey(pw, salt);
        MessageDigest messageDigest = sha256Digest();
        for (int i = 0; i <= iterations; i++) {
            messageDigest.update(stretchedKey);
            stretchedKey = messageDigest.digest();
        }
        messageDigest.update(stretchedKey);
        return stretchedKey;
    }
