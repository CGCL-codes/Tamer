    public static byte[] hash(char[] password) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        for (char ch : password) {
            byte b1 = (byte) ((ch >> 8) & 0x00FF);
            byte b2 = (byte) (ch & 0x00FF);
            md.update(b1);
            md.update(b2);
        }
        return md.digest();
    }
