    public static boolean equal(final InputStream a, final InputStream b) {
        final MD5 md5 = new MD5();
        try {
            final String hashA = md5.digest(a, new HexConverter());
            final String hashB = md5.digest(b, new HexConverter());
            return hashA.equals(hashB);
        } catch (Exception ex) {
            return false;
        }
    }
