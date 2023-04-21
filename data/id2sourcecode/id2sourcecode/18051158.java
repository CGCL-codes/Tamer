    public static String encryptString(String password) {
        if (password == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] encryptMsg = md.digest(password.getBytes());
            BigInteger temp = new BigInteger(encryptMsg);
            return temp.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
