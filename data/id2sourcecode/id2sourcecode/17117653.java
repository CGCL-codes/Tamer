    public static synchronized byte[] digest(String message, byte[] salt) {
        try {
            byte[] msg = message.getBytes("UTF-8");
            md.update(salt);
            md.update(msg);
        } catch (Exception ex) {
        }
        return md.digest();
    }
