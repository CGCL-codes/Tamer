    public static String passwordHash(String input) {
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            byte[] out = m.digest(input.getBytes());
            return new String(Base64.encodeBase64(out));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
