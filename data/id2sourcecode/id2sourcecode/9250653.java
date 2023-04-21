    public static String encrypt(String data, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(algorithm);
        byte result[] = md5.digest(UTF8.encode(data));
        StringBuffer sb = new StringBuffer();
        for (byte b : result) {
            String s = Integer.toHexString(b);
            int length = s.length();
            if (length >= 2) {
                sb.append(s.substring(length - 2, length));
            } else {
                sb.append("0");
                sb.append(s);
            }
        }
        return sb.toString();
    }
