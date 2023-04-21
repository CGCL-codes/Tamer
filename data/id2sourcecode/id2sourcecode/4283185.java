    public static char[] getHash(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        byte[] data = text.getBytes("utf-8");
        MessageDigest md = MessageDigest.getInstance("MD5");
        for (byte b : data) {
            md.update(b);
        }
        byte[] digest = md.digest();
        char[] hash = new char[digest.length];
        int index = 0;
        for (byte b : digest) {
            hash[index++] = (char) b;
        }
        return hash;
    }
