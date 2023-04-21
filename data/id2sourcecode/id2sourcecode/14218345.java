    public static String getMD5(String plaintext) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("md5");
            String digest_data = plaintext;
            byte[] data_bytes = md5.digest(digest_data.getBytes());
            StringBuffer hexString = new StringBuffer();
            String hex = null;
            for (int i = 0; i < data_bytes.length; i++) {
                hex = Integer.toHexString(0xFF & data_bytes[i]);
                if (hex.length() < 2) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
