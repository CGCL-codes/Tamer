    public static String digest(String aValue) {
        aValue = aValue.trim();
        byte value[];
        try {
            value = aValue.getBytes(encodingCharset);
        } catch (UnsupportedEncodingException e) {
            value = aValue.getBytes();
        }
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(" SHA ");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return toHex(md.digest(value));
    }
