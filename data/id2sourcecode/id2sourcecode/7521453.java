    public static String digestMerId(String inputId) {
        String result = inputId;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] resultingBytes = md5.digest(inputId.getBytes());
            result = Base64.encodeBase64URLSafeString(resultingBytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }
