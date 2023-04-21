    public static String md5(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] b = md.digest(msg.getBytes());
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
