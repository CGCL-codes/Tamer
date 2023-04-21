    public static byte[] getMd5(final InputStream stream) throws IOException {
        try {
            MessageDigest md5DigestLocal = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int readed = -1;
            while ((readed = stream.read(buffer)) != -1) {
                md5DigestLocal.update(buffer, 0, readed);
            }
            return md5DigestLocal.digest();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
