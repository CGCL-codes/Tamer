    public static String digest(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] digest = md.digest(data.getBytes());
        String b64 = Base64.encodeBytes(digest);
        return b64;
    }
