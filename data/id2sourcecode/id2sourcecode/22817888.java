    public static String md5(final byte[] input) {
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(input);
            final byte[] result = md5.digest();
            final StringBuilder hexString = new StringBuilder();
            for (int i = 0; i < result.length; i++) {
                hexString.append(Integer.toHexString(0xFF & result[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
