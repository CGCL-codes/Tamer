    public static byte[] md5(byte[] m) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(m);
            return algorithm.digest();
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
