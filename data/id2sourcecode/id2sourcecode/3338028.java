    public String encryptPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        if (key != null) {
            password = password + key;
        }
        byte[] bytes = md.digest(password.getBytes());
        return bytesToHexString(bytes);
    }
