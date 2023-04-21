    public static String generate(int length) throws NoSuchAlgorithmException {
        StringBuilder string = new StringBuilder(length);
        byte[] buffer = new byte[32];
        while (string.length() < length) {
            SecurityUtilities.getSecureRandom().nextBytes(buffer);
            string.append(StringUtilities.toHex(SecurityUtilities.createMessageDigest().digest(buffer)));
        }
        string.setLength(length);
        return string.toString();
    }
