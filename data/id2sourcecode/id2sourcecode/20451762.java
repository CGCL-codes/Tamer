    private byte[] messageDigest(String file) throws Exception {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        byte md[] = new byte[8192];
        InputStream in = new FileInputStream(file);
        for (int n = 0; (n = in.read(md)) > -1; ) messagedigest.update(md, 0, n);
        return messagedigest.digest();
    }
