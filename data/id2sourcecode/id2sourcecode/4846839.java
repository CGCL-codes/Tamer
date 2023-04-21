    public static String crypt(String passwd) throws NoSuchAlgorithmException {
        return "{SHA}" + new sun.misc.BASE64Encoder().encode(java.security.MessageDigest.getInstance("SHA1").digest(passwd.getBytes()));
    }
