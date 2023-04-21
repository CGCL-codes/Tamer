    private static long computeFormatID(String format) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] b = md.digest(format.getBytes("UTF-8"));
            return ((b[7] & 0xFFL) << 0) + ((b[6] & 0xFFL) << 8) + ((b[5] & 0xFFL) << 16) + ((b[4] & 0xFFL) << 24) + ((b[3] & 0xFFL) << 32) + ((b[2] & 0xFFL) << 40) + ((b[1] & 0xFFL) << 48) + ((b[0] & 0xFFL) << 56);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }
