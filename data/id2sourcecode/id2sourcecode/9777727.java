    public static final String calc(String algorithm, String value) {
        ArgumentHelper.checkForContent(algorithm);
        ArgumentHelper.checkForContent(value);
        final StringBuffer sb = new StringBuffer("");
        try {
            final MessageDigest md = MessageDigest.getInstance(algorithm);
            final byte[] digest = md.digest(value.getBytes());
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
        } catch (NoSuchAlgorithmException ex) {
            throw new AssertionError(WARN_NO_SUCH_ALGORITHM);
        }
        return sb.toString();
    }
