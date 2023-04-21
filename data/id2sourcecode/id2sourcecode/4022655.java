    public static String sha(byte[] source) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            byte[] bytes = md.digest(source);
            return getHexString(bytes);
        } catch (Throwable t) {
            if (m_logger.isDebugEnabled()) {
                m_logger.debug("Unable to calculate SHA", t);
            }
            return null;
        }
    }
