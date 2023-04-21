    public static String shaEncode(String text) {
        if (text == null) {
            return text;
        }
        byte[] buf = text.getBytes();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(buf);
            return ERXStringUtilities.byteArrayToHexString(md.digest());
        } catch (java.security.NoSuchAlgorithmException ex) {
            throw new NSForwardException(ex, "Couldn't find the SHA algorithm; perhaps you do not have the SunJCE security provider installed properly?");
        }
    }
