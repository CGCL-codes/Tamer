    public static final byte[] digestBytes(byte[] bData) throws RuntimeException {
        synchronized (MD5.class) {
            if (md5 == null) {
                try {
                    md5 = MessageDigest.getInstance("MD5");
                } catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e.toString());
                }
            }
            return md5.digest(bData);
        }
    }
