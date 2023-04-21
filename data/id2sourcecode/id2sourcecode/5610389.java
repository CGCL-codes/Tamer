    public static String hexString(MessageDigest digest) {
        byte[] messageDigest = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < messageDigest.length; i++) {
            String hex = Integer.toHexString(0xFF & messageDigest[i]);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
