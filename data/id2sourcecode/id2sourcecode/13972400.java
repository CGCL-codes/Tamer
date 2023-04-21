    public static String hashPass(String password) {
        byte[] digestedPass = null;
        try {
            byte[] bytesOfMessage;
            bytesOfMessage = password.getBytes();
            MessageDigest md = MessageDigest.getInstance("MD5");
            digestedPass = md.digest(bytesOfMessage);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return String.format("%x", new BigInteger(digestedPass));
    }
