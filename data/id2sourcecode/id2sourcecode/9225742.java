    public static String checksum(byte[] data) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException noMD5) {
            try {
                md = MessageDigest.getInstance("SHA");
            } catch (NoSuchAlgorithmException noSha) {
                throw new RuntimeException("No suitable MessageDigest found!");
            }
        }
        byte[] hash = md.digest(data);
        StringBuilder hexString = new StringBuilder(hash.length * 2 + 2);
        for (byte b : hash) {
            hexString.append(Integer.toHexString(0xFF & b));
        }
        return hexString.toString();
    }
