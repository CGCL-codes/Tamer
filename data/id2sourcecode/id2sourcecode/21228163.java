    public static String encode(String origin) {
        String resultString = origin;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = dumpBytes(md.digest(resultString.getBytes("UTF-8")));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString.toLowerCase();
    }
