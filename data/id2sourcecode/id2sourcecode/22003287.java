    public static String getChecksum(byte[] input) {
        MessageDigest checksum;
        try {
            checksum = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        checksum.update(input);
        byte[] byteDigest = checksum.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteDigest.length; i++) {
            String hex = Integer.toHexString(0xff & byteDigest[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return new String(sb.toString());
    }
