    public static String digestString(String s) {
        StringBuffer result = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = md.digest(s.getBytes());
            for (int i = 0; i < b.length; i++) {
                result.append(b[i]);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return result.toString();
    }
