    public static String encodeMD5(String msg) {
        String digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte msgBytes[] = msg.getBytes();
            md.update(msgBytes);
            digest = Base64.encodeBytes(md.digest());
        } catch (Exception e) {
            log.error(e);
        }
        return digest;
    }
