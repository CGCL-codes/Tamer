    public static synchronized byte[] md5(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(source);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
