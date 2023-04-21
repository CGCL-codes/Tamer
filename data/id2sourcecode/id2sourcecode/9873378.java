    private String hash(String password, String hashFunctionName) {
        String hexString = "";
        try {
            MessageDigest digest;
            digest = MessageDigest.getInstance(PASSWORD_HASH_FUNC);
            byte[] byteHash = digest.digest(password.getBytes());
            for (int i = 0; i < byteHash.length; i++) {
                hexString += Integer.toHexString(0xFF & byteHash[i]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString;
    }
