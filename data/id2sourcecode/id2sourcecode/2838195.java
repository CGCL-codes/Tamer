    public byte[] getRawDigest(String text) {
        byte[] digest = null;
        if (StringUtils.isNotBlank(text)) {
            try {
                MessageDigest messageDigester = MessageDigest.getInstance("SHA");
                digest = messageDigester.digest(text.getBytes());
            } catch (NoSuchAlgorithmException e) {
                LOGGER.error(ExceptionUtils.getFullStackTrace(e));
            }
        }
        return digest;
    }
