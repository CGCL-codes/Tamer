    public static byte[] encrypt(byte[] x) throws Exception {
        java.security.MessageDigest d = null;
        d = java.security.MessageDigest.getInstance("SHA-1");
        d.reset();
        d.update(x);
        return d.digest();
    }
