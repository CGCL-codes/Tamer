    protected static final String getMD5Hash(String text) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            StringBuffer buffer = new StringBuffer();
            byte bytes[] = text.replace("\\", "\\\\").replace("\"", "\\\"").getBytes();
            byte[] md5 = messageDigest.digest(bytes);
            for (int i = 0; i < md5.length; i++) {
                String temp = "0" + Integer.toHexString((0xff & md5[i]));
                buffer.append(temp.substring(temp.length() - 2));
            }
            return buffer.toString();
        } catch (Exception exception) {
            return "";
        }
    }
