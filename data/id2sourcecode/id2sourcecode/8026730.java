    public static String sha1HashAsHexString(String s) {
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            sha1.update(IOUtil.encodeString(s));
            return IOUtil.printByteArray(sha1.digest(), false, false);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("No cryptographic provider for SHA-1 configured");
        }
    }
