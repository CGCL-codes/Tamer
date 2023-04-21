    public static String getChecksum(ByteArrayOutputStream stream, String algorithm) throws NoSuchAlgorithmException {
        MessageDigest checksum = null;
        checksum = MessageDigest.getInstance(algorithm);
        byte[] buf = stream.toByteArray();
        checksum.update(buf);
        byte[] byteDigest = checksum.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteDigest.length; i++) {
            String hex = Integer.toHexString(0xff & byteDigest[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return new String(sb.toString());
    }
