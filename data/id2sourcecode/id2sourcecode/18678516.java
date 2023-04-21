    public String addDigest(String message, String secretkey, String remoteAddr) {
        md.update(message.getBytes());
        if (remoteAddr != null) md.update(remoteAddr.getBytes());
        byte digest[] = md.digest(secretkey.getBytes());
        return byte2hexstring(digest) + ":" + message;
    }
