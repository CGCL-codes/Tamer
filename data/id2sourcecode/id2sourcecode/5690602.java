    public static String md5(byte[] msgData) {
        String md5;
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] md5Bytes = digest.digest(msgData);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = ((int) md5Bytes[i]) & 0xff;
                if (val < 16) hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            md5 = hexValue.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
        return md5;
    }
