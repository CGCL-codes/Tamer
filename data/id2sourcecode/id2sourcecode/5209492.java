    public static byte[] createMD5(final InputStream data) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        final byte buf[] = new byte[8192];
        int bytes = 0;
        while ((bytes = data.read(buf)) != -1) {
            md5.update(buf, 0, bytes);
        }
        return md5.digest();
    }
