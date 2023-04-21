    public static String getHashAsHex(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance(SHA_1);
            return convertToHex(md.digest(text.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            log.error("SHA-1 algorithm for signing API data not found!");
            return null;
        }
    }
