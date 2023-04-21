    public String encodeMd5Base64(String s) throws Exception {
        byte[] buf = s.getBytes();
        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
        md.update(buf, 0, buf.length);
        buf = new byte[16];
        md.digest(buf, 0, buf.length);
        return base64Encode(buf);
    }
