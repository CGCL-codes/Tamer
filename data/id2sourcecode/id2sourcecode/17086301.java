    public void setPassword(String textPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
            byte[] b = digest.digest(UTF8.toUTF8(textPassword));
            this.password = toHexString(b);
        } catch (NoSuchAlgorithmException e) {
            this.password = textPassword;
            e.printStackTrace(System.err);
        }
    }
