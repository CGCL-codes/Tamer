    public static String md5(final InputStream source) throws RuntimeException {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] buf = new byte[8096];
            int len;
            while ((len = source.read(buf)) >= 0) {
                md.update(buf, 0, len);
            }
            return CheckSum.hex(md.digest());
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
