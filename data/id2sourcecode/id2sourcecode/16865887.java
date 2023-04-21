    private synchronized String encodeString(String msg) {
        md.update(msg.getBytes());
        return toHexString(md.digest());
    }
