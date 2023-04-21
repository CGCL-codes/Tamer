    public boolean checkPassword(String textPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            byte[] b = digest.digest(UTF8.toUTF8(textPassword));
            return password.equals(toHexString(b));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
            return password.equals(textPassword);
        }
    }
