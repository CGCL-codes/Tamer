    private String createCustomCookieValue() {
        String value = null;
        byte[] buffer = null;
        MessageDigest md = null;
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            md = MessageDigest.getInstance("SHA");
            buffer = new Date().toString().getBytes();
            md.update(buffer);
            value = encoder.encode(md.digest());
            original = value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }
