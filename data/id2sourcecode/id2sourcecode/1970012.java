    private byte[] getMD5(File file) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        DigestInputStream in = new DigestInputStream(new FileInputStream(file), md);
        byte[] buffer = new byte[BUFLEN];
        while (in.read(buffer) != -1) ;
        byte[] raw = md.digest();
        return raw;
    }
