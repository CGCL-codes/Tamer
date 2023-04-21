    public static String MD5(String origin) {
        String resultString = null;
        if (origin == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            resultString = byteArrayToHexString(md.digest(origin.getBytes()));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return resultString;
    }
