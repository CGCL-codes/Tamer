    private static String computeSha1OfByteArray(final byte[] message) throws UnsupportedOperationException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(message);
            byte[] res = md.digest();
            return toHexString(res);
        } catch (NoSuchAlgorithmException ex) {
            throw new UnsupportedOperationException(ex);
        }
    }
