    public static String getMD5(String s) {
        byte[] uniqueKey;
        uniqueKey = s.getBytes();
        try {
            uniqueKey = s.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        byte[] hash = null;
        try {
            hash = MessageDigest.getInstance("MD5").digest(uniqueKey);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("MD5 non supporté par la VM");
        }
        StringBuilder hashString = new StringBuilder();
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(hash[i]);
            if (hex.length() == 1) {
                hashString.append('0');
                hashString.append(hex.charAt(hex.length() - 1));
            } else hashString.append(hex.substring(hex.length() - 2));
        }
        return hashString.toString();
    }
