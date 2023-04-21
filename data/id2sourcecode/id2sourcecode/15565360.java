    public static String encrypt(String string) {
        if (string == null) {
            return null;
        }
        String encoding = OdbConfiguration.getDatabaseCharacterEncoding();
        checkInit();
        try {
            md.update(string.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        try {
            return new String(md.digest(), encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new String(md.digest());
    }
