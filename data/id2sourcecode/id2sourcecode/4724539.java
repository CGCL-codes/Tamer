    public static String getMD5Digest(String data) {
        if (data == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                String s = Integer.toHexString(0xFF & b);
                if (s.length() == 1) {
                    sb.append('0');
                }
                sb.append(s);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            return null;
        }
    }
