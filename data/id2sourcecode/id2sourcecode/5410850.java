    public static String encrypt(final String anOrig) {
        try {
            final byte[] todig = anOrig.getBytes(PASSWORD_ENCODING);
            final byte[] res = getMessageDigest().digest(todig);
            return new String(res, PASSWORD_ENCODING);
        } catch (final UnsupportedEncodingException uoe) {
            throw new UnsupportedOperationException("encoding:" + PASSWORD_ENCODING + " unknown");
        }
    }
