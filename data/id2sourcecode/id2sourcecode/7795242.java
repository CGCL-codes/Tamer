    public static final String enc(String string) {
        String ret = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] digest = md.digest(string.getBytes("UTF-8"));
            ret = new String(Base64.encodeBytes(digest));
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA not available, setting default password", e);
            ret = DEFAULT_PASSWD;
        } catch (UnsupportedEncodingException e) {
            log.error("UTF-8 not supported, setting default password", e);
            ret = DEFAULT_PASSWD;
        }
        return ret;
    }
