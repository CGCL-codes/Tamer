    protected String getMD5Hash(MessageDigest md5) {
        StringBuffer sb = new StringBuffer();
        try {
            byte[] md5Byte = md5.digest();
            for (int i = 0; i < md5Byte.length; i++) {
                sb.append(Integer.toHexString((0xff & md5Byte[i])));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(sb.toString());
    }
