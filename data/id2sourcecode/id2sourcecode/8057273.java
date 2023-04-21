    private String getHash(String password) {
        byte[] byteDigest = null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.reset();
            byteDigest = digest.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteToHex(byteDigest);
    }
