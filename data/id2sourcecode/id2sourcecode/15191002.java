    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            return null;
        } else {
            try {
                MessageDigest digest = MessageDigest.getInstance("SHA-1");
                digest.update(stringToBytes(plainPassword));
                byte[] result = digest.digest();
                return bytesToString(result);
            } catch (NoSuchAlgorithmException e) {
                return String.valueOf(plainPassword.hashCode());
            }
        }
    }
