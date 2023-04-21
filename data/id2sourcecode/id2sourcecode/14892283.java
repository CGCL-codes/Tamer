    public static String toSha1(String target, String charsetName) throws UnsupportedEncodingException {
        try {
            return Base64.encode(MessageDigest.getInstance("SHA1").digest(target.getBytes(charsetName)), false);
        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("No SHA1 algorithm, unable to compute SHA1");
        }
    }
