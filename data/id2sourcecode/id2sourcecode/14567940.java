    private static byte[] loginHash(String name, String password, byte[] salt) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(salt);
            digest.update(baseHash(name, password));
            return digest.digest();
        } catch (NoSuchAlgorithmException ex) {
            d("MD5 algorithm not found!");
            throw new RuntimeException("MD5 algorithm not found! Unable to authenticate");
        }
    }
