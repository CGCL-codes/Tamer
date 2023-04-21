    public static String md5(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source);
            return getHexString(bytes);
        } catch (Throwable t) {
            if (m_logger.isDebugEnabled()) {
                m_logger.debug("Unable to calculate MD5", t);
            }
            return null;
        }
    }
