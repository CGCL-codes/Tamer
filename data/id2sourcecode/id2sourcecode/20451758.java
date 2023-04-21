    public static String toMD5(String string) throws PasswordException {
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (Exception e) {
            e.printStackTrace();
            throw new PasswordException("MD5 Algorithm not found: " + e.getMessage());
        }
        String hash = toHexString(md5.digest(string.getBytes()));
        return hash;
    }
