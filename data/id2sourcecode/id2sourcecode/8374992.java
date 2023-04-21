    public static String shaEncode(String string) {
        byte[] buf = string.getBytes();
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new NSForwardException(e);
        }
        md.update(buf);
        return bytesToString(md.digest());
    }
