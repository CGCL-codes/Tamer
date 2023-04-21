    private String calcFingerprint(final byte[] data) {
        try {
            final MessageDigest md = MessageDigest.getInstance("SHA1");
            return new String(Hex.encode(md.digest(data)));
        } catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
