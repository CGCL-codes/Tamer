    public static String encodeMD5(String text) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            baos.write(text == null ? new byte[0] : text.getBytes("UTF8"));
        } catch (IOException ioe) {
        }
        byte[] hash = null;
        MessageDigest digest = allocateDigestInstance(MD5_ALGORITHIM);
        hash = digest.digest(baos.toByteArray());
        return encodeMD5Data(hash);
    }
