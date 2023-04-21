    public static String MD5Encode(String origin) {
        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = HEXEncoder.encode(md.digest(resultString.getBytes()));
        } catch (Exception ex) {
        }
        return resultString;
    }
