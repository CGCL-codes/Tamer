    public static String getChecksum(InputStream stream, String algorithm) throws IOException, NoSuchAlgorithmException {
        MessageDigest checksum = null;
        checksum = MessageDigest.getInstance(algorithm);
        byte[] data = new byte[128];
        while (true) {
            int bytesRead = stream.read(data);
            if (bytesRead < 0) {
                break;
            }
            checksum.update(data, 0, bytesRead);
        }
        byte[] byteDigest = checksum.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteDigest.length; i++) {
            String hex = Integer.toHexString(0xff & byteDigest[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return new String(sb.toString());
    }
