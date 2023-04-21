    public static String PBKDF2(String hashAlgorithm, String password, String salt, long iterationCount) throws NoSuchAlgorithmException {
        if (hashAlgorithm == null) throw new IllegalArgumentException("The hash algorithm cannot be null");
        if (iterationCount < 1) throw new IllegalArgumentException("The iteration count for PBKDF2 must be greater than 0");
        MessageDigest messageDigest = MessageDigest.getInstance(hashAlgorithm);
        byte[] hashBytes = (password + salt).getBytes();
        for (int c = 0; c < iterationCount; c++) {
            hashBytes = messageDigest.digest(hashBytes);
        }
        String passwordHash = new String(Hex.encodeHex(hashBytes));
        return passwordHash;
    }
