    public static String computeMd5Hex(String passwd) {
        if (passwd == null) throw new IllegalArgumentException();
        final int GUARANTEED_LENGTH = 32;
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(passwd.getBytes());
            String output = new java.math.BigInteger(1, bytes).toString(16);
            while (output.length() < GUARANTEED_LENGTH) output = "0" + output;
            assert (output.length() == GUARANTEED_LENGTH);
            return output;
        } catch (java.security.NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
