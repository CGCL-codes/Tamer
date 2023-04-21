    public static String toMD5(String origin) {
        String result = null;
        try {
            result = new String(origin);
            result = PassWordUtils.toHexString(digest.digest(result.getBytes()));
        } catch (Exception ex) {
            return null;
        }
        return result;
    }
