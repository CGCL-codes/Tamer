    public static final String digestMD5(String message) {
        byte[] digest = new byte[0];
        StringBuffer digestMessage = new StringBuffer();
        try {
            digest = MessageDigest.getInstance("MD5").digest(message.getBytes("ISO-8859-1"));
            for (int i = 0; i < digest.length; i++) {
                digestMessage.append(Integer.toString((digest[i] & 0xf0) >> 4, 16));
                digestMessage.append(Integer.toString(digest[i] & 0x0f, 16));
            }
        } catch (Exception e) {
            log.error(e);
        }
        return digestMessage.toString();
    }
