    public static String md5(String input) {
        if (input == null) return null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] output = md.digest(input.getBytes());
            return new String(output, "ISO-8859-1");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
