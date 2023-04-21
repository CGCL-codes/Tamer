    public static synchronized byte[] saltAndDigest(String message, byte[] digest) {
        byte[] salt = Long.toString(System.currentTimeMillis()).getBytes();
        try {
            byte[] msg = message.getBytes("UTF-8");
            md.update(salt);
            md.update(msg);
            md.digest(digest, 0, 20);
        } catch (Exception ex) {
        }
        return salt;
    }
