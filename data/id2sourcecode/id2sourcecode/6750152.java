    public static byte[] sha1hash(String str) {
        byte[] email_hash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            email_hash = md.digest(str.getBytes("UTF-8"));
            email_hash = md.digest(email_hash);
        } catch (Exception ex) {
            Logger.errorMessage("Exception: " + ex.getMessage());
            email_hash = str.getBytes();
        }
        return email_hash;
    }
