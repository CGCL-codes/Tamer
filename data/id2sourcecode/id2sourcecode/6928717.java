    public String getPasswordHash() {
        try {
            char[] chars = passwordField.getPassword();
            byte[] bytes = new byte[chars.length];
            for (int i = 0; i < chars.length; ++i) {
                bytes[i] = (byte) chars[i];
            }
            return new String(MessageDigest.getInstance("MD5").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
