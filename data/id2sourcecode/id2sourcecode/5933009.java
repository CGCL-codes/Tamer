    public static final String MD5File(File file) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance(HASH_ALGORITHM);
        md5.reset();
        try {
            byte[] buffer = new byte[1024];
            InputStream in = new BufferedInputStream(new FileInputStream(file));
            int c = 0;
            while ((c = in.read(buffer)) != -1) {
                md5.update(buffer, 0, c);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return asHex(md5.digest());
    }
