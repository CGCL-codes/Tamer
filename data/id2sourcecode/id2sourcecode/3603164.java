    public static String constructHash(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Base64.encodeBase64URLSafeString(md.digest(input.getBytes()));
    }
