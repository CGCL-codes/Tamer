    public static String digest(String plain) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException exception) {
        }
        byte[] digest = null;
        try {
            digest = md.digest(plain.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException exception) {
        }
        StringBuffer hex = new StringBuffer(digest.length * 2);
        for (int i = 0; i < digest.length; i++) {
            byte b = digest[i];
            hex.append(hexTable[(b >> 4) & 15]);
            hex.append(hexTable[b & 15]);
        }
        return hex.toString();
    }
