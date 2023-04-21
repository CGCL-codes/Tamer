    public byte[] createChecksum(byte[] fileByte) throws Exception {
        MessageDigest complete = MessageDigest.getInstance("MD5");
        complete.digest(fileByte);
        return complete.digest();
    }
