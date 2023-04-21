    public String getMD5Hash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytesToEncrypt = md.digest(password.getBytes());
            return new BigInteger(1, bytesToEncrypt).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
