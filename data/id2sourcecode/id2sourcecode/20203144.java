    public static long passwordHash(String password) {
        final long DEFAULT_HASH = 98234782;
        byte[] byteHash = null;
        String hexString = null;
        if (password == null || password.equals("")) {
            return DEFAULT_HASH;
        }
        try {
            byteHash = MessageDigest.getInstance("MD5").digest(password.getBytes());
            hexString = getHexString(byteHash);
            hexString = hexString.substring(0, 15);
            return Long.parseLong(hexString, 16);
        } catch (NoSuchAlgorithmException nsaEx) {
            return DEFAULT_HASH;
        }
    }
