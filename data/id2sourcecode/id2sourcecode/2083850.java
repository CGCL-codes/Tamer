    public static String hash(String pass) {
        MessageDigest algorithm;
        try {
            algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            byte[] hash = algorithm.digest(pass.getBytes());
            hash = algorithm.digest(hash);
            StringBuffer buf = new StringBuffer();
            for (byte aHash : hash) {
                buf.append(Byte.toString(aHash));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            return pass;
        }
    }
