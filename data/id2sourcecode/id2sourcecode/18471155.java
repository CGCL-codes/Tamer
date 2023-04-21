    public static byte[] hash(InputStream is, String algorithm, byte[] buf) throws IOException, GeneralSecurityException {
        lazyLoad();
        MessageDigest md = MessageDigest.getInstance(algorithm, "BC");
        for (int bytesRead = is.read(buf); bytesRead > 0; bytesRead = is.read(buf)) {
            md.update(buf, 0, bytesRead);
        }
        return md.digest();
    }
