    public static byte[] generateSHA1Fingerprint(byte[] ba) {
        log.debug(">generateSHA1Fingerprint");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(ba);
        } catch (NoSuchAlgorithmException nsae) {
            log.error("SHA1 algorithm not supported", nsae);
        }
        log.debug("<generateSHA1Fingerprint");
        return null;
    }
