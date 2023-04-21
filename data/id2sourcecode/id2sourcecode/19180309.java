    public static String encrypt(String s) {
        try {
            byte[] message = null;
            MessageDigest dig = MessageDigest.getInstance("MD5");
            String encoding = "CP1252";
            try {
                message = s.getBytes(encoding);
                encoding = String.valueOf(System.getProperty("file.encoding"));
            } catch (UnsupportedEncodingException e) {
                message = s.getBytes();
            }
            dig.update(message);
            String encrYptEd = hex(dig.digest());
            return encrYptEd;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
