    public static byte[] digest(final InputStream[] in) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            byte[] b = new byte[8192];
            try {
                for (final InputStream anIn : in) for (int l; (l = anIn.read(b)) > 0; ) messageDigest.update(b, 0, l);
            } finally {
                for (InputStream anIn : in) try {
                    anIn.close();
                } catch (Exception e) {
                }
            }
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
