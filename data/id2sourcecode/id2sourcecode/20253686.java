    public static byte[] generateSHA1Fingerprint(byte[] ba) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA1");
            return md.digest(ba);
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("SHA1 algorithm not supported" + nsae.getLocalizedMessage());
        }
        return null;
    }
