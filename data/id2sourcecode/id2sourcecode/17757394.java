    public static String createRasterID(String instring) {
        StringBuffer ID = new StringBuffer();
        ID.append("landserf_");
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte digest[] = md.digest(instring.getBytes());
            for (int i = 0; i < digest.length; i++) ID.append(Integer.toHexString(digest[i] & 0xff));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ID.toString();
    }
