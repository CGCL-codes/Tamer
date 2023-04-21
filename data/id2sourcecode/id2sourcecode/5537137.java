    public static String getDigest(byte[] code) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] passwordMD5Byte = md.digest(code);
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < passwordMD5Byte.length; i++) sb.append(Integer.toHexString(passwordMD5Byte[i] & 0XFF));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error(e);
            return null;
        }
    }
