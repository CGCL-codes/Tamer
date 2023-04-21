    public static final String dump(MessageDigest md) {
        String result;
        try {
            result = Util.dumpString(((MessageDigest) md.clone()).digest());
        } catch (Exception ignored) {
            result = "...";
        }
        return result;
    }
