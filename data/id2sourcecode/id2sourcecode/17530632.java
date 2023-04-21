    public static final String dump(MessageDigest md) {
        String result;
        try {
            result = dumpString(((MessageDigest) md.clone()).digest());
        } catch (Exception ignored) {
            result = "...";
        }
        return result;
    }
