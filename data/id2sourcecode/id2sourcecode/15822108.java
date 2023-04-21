    public static String md5(byte[] in) {
        MessageDigest md5 = null;
        try {
            if (md5 == null) md5 = MessageDigest.getInstance("MD5");
            md5.update(in);
            byte[] theDigest = md5.digest();
            if (theDigest == null) return null;
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < theDigest.length; i++) {
                sb.append(toUnsignedString(theDigest[i] & 0x00FF, 4, 2));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
