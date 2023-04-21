    private static String hashValue(final String rawResponse) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance("SHA1");
        final byte[] hashedAnswer = md.digest(rawResponse.getBytes());
        return Base64Util.encodeBytes(hashedAnswer);
    }
