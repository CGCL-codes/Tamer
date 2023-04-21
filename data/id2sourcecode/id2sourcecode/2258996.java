    public static String calculateHash(String message) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5 = md.digest(message.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md5.length; i++) {
                String tmpStr = "0" + Integer.toHexString((0xff & md5[i]));
                sb.append(tmpStr.substring(tmpStr.length() - 2));
            }
            return sb.toString();
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
