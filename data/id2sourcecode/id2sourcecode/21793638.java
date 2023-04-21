    public static String md5(String s) {
        try {
            byte[] bytes = digest.digest(s.getBytes("UTF-8"));
            StringBuilder b = new StringBuilder(32);
            for (byte aByte : bytes) {
                String hex = Integer.toHexString((int) aByte & 0xFF);
                if (hex.length() == 1) b.append('0');
                b.append(hex);
            }
            return b.toString();
        } catch (UnsupportedEncodingException e) {
        }
        return null;
    }
