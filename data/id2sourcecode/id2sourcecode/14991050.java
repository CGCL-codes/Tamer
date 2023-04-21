    public static synchronized byte[] sha(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return md.digest(source);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
