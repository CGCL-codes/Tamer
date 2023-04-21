    public static String hashCrypt(String alg, String text, boolean base64) {
        byte[] textBytes;
        try {
            textBytes = text.getBytes("UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            textBytes = text.getBytes();
        }
        try {
            java.security.MessageDigest hasher = java.security.MessageDigest.getInstance(alg);
            hasher.update(textBytes, 0, textBytes.length);
            if (base64) {
                return Base64EncodeFilter.base64(hasher.digest());
            } else {
                return new java.math.BigInteger(1, hasher.digest()).toString(16);
            }
        } catch (java.security.NoSuchAlgorithmException e) {
            return text;
        }
    }
