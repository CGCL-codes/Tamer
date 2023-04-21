    public static String md5(String toSign) {
        byte[] stringBytes;
        try {
            stringBytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            stringBytes = toSign.getBytes();
        }
        byte[] hashed = md5.digest(stringBytes);
        StringBuffer rv = new StringBuffer();
        for (int i = 0; i < hashed.length; i++) {
            rv.append(hexChars[0x00000F & hashed[i] >> 4]);
            rv.append(hexChars[0x00000F & hashed[i]]);
        }
        return rv.toString();
    }
