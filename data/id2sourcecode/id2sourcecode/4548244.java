    protected SecretKey makeKey(String userid) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("md5");
        byte[] k = md5.digest(userid.getBytes());
        return new SecretKeySpec(k, 0, 8, "DES");
    }
