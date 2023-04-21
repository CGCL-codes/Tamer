    public String encryptPassword(String password) {
        try {
            messageDigest.update(password.getBytes("UTF-8"));
            byte digest[] = messageDigest.digest();
            return Base64Encoder.encode(digest);
        } catch (Exception e) {
            return password;
        }
    }
