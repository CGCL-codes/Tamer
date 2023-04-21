    public static byte[] hashString(Digest d, String s) {
        try {
            byte[] sbytes = s.getBytes("UTF8");
            d.update(sbytes, 0, sbytes.length);
            return d.digest();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception thrown in hashString(Digest d, String s)", e);
        }
        return null;
    }
