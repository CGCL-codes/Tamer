    public static byte[] certHash(byte[] cert) {
        if (cert == null) {
            return null;
        }
        byte[] hash = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            hash = md.digest(cert);
        } catch (Throwable t) {
        }
        return hash;
    }
