    protected String calcMD5(File file) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        FileInputStream input = new FileInputStream(file);
        byte[] buf = new byte[1024];
        while (input.available() > 0) {
            int res = input.read(buf, 0, buf.length);
            md.update(buf, 0, res);
        }
        byte[] md5 = md.digest();
        return bytesToHexString(md5);
    }
