    public String md5(String source) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(source.getBytes());
            return getHexString(bytes);
        } catch (Throwable t) {
            return null;
        }
    }
