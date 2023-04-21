    protected String processDigest(MessageDigest digest, String str) {
        byte[] srcBytes = str.getBytes();
        byte[] dstBytes = digest.digest(srcBytes);
        StringBuilder hexBuf = new StringBuilder(dstBytes.length * 2);
        for (int i = 0; i < dstBytes.length; ++i) {
            String hex = Integer.toHexString(dstBytes[i] & 0xFF).toUpperCase();
            if (hex.length() == 1) {
                hexBuf.append('0');
            }
            hexBuf.append(hex);
        }
        return hexBuf.toString();
    }
