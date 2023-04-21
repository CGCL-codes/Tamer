    public static String encodeMD5Base64(final String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(password.getBytes("UTF-8"));
            return base64Encode(digest);
        } catch (NoSuchAlgorithmException e) {
            RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        } catch (UnsupportedEncodingException e) {
            RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
