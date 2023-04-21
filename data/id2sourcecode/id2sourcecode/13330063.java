    public static String getFileMD5(InputStream in) {
        MessageDigest digest = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
        } catch (Exception e) {
            logger.error("IOException: ", e);
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                logger.error("IOException: ", e);
            }
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16).toUpperCase();
    }
