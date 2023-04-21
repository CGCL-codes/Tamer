    public static byte[] digestNoClose(final InputStream[] in) throws IOException {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            byte[] b = new byte[8192];
            for (final InputStream anIn : in) for (int l; (l = anIn.read(b)) > 0; ) messageDigest.update(b, 0, l);
            return messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
