    public static byte[] generateMD5Fingerprint(byte[] ba) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(ba);
        } catch (NoSuchAlgorithmException nsae) {
            System.err.println("MD5 algorithm not supported" + nsae.getLocalizedMessage());
        }
        return null;
    }
