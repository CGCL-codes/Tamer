    public String getMD5Checksum(byte[] contents) throws NoSuchAlgorithmException, IOException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] buffer = new byte[2048];
        int ch = 0;
        ByteArrayInputStream bin = new ByteArrayInputStream(contents);
        while ((ch = bin.read(buffer)) > 0) {
            md5.update(buffer, 0, ch);
        }
        bin.close();
        buffer = md5.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buffer.length; i++) {
            byte b = buffer[i];
            sb.append(Integer.toString((b & 0xff) + 0x100, 16));
        }
        return sb.toString();
    }
