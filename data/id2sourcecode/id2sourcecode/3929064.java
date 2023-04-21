    public static String md5sum(String string) {
        try {
            byte[] stringArray;
            try {
                stringArray = string.getBytes("UTF-8");
            } catch (Exception ignored) {
                stringArray = string.getBytes();
            }
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] raw = md.digest(stringArray);
            StringBuffer result = new StringBuffer(32);
            for (byte b : raw) {
                int i = (b & 0xFF);
                if (i < 16) {
                    result.append("0");
                }
                result.append(Integer.toHexString(i));
            }
            return result.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
