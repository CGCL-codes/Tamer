    private static byte[] calculateMD5Checksum(final InputStream in) throws IOException {
        final MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 checksum not available", e);
        }
        final InputStream cin = new DigestInputStream(in, md5);
        final byte[] buffer = new byte[512];
        try {
            while (cin.read(buffer) != -1) {
            }
        } finally {
            cin.close();
        }
        return md5.digest();
    }
