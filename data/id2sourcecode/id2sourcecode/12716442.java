    public static String md5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            StringBuilder result = new StringBuilder(new BigInteger(1, md.digest(value.toString().getBytes())).toString(16));
            while (result.length() < 32) {
                result.insert(0, "0");
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
