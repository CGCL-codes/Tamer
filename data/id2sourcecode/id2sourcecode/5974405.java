    public static String sha512Encrypt(String clearText) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        return bytesToHexString(md.digest(clearText.getBytes()));
    }
