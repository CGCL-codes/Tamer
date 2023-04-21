    private static String updateDigest(MessageDigest digest, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[2048];
        int read = 0;
        while ((read = inputStream.read(buffer)) > 0) {
            digest.update(buffer, 0, read);
        }
        return new String(Base64.encode(digest.digest()));
    }
