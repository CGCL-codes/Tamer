    public String md5(String str) {
        String s = str;
        if (s == null) {
            return "";
        } else {
            String value = null;
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ex) {
            }
            sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
            try {
                return new String(md5.digest(s.getBytes()), "ascii");
            } catch (Exception ex) {
            }
            return value;
        }
    }
