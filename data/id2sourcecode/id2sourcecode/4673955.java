    public static String SHA256(String message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        final byte[] data = message.getBytes(Charset.forName("UTF8"));
        final byte[] digest = messageDigest.digest(data);
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            byte b = digest[i];
            result.append(Integer.toHexString(0xFF & b));
        }
        return result.toString();
    }
