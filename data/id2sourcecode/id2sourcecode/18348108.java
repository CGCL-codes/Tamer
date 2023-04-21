    public static byte[] md5(InputStream in) throws IOException {
        try {
            java.security.MessageDigest md5 = java.security.MessageDigest.getInstance("MD5");
            byte[] buf = new byte[50 * 1024];
            int numRead;
            while ((numRead = in.read(buf)) != -1) {
                md5.update(buf, 0, numRead);
            }
            return md5.digest();
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new NSForwardException(e);
        }
    }
