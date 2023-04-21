    public static String getMD5(File file) throws IOException {
        MessageDigest digest = digests.get("MD5");
        if (digest == null) {
            try {
                digest = MessageDigest.getInstance("MD5");
                digests.put("MD5", digest);
            } catch (NoSuchAlgorithmException nsae) {
                nsae.printStackTrace();
                return null;
            }
        }
        FileInputStream fis = null;
        fis = new FileInputStream(file);
        byte[] buffer = new byte[8192];
        int length = -1;
        while ((length = fis.read(buffer)) != -1) {
            digest.update(buffer, 0, length);
        }
        return encodeHex(digest.digest());
    }
