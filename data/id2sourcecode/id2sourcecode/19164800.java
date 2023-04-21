    private static String SHKgenerate(String source) {
        byte[] bytes = null;
        source = source.replaceAll("[\\s]+", " ");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            bytes = md.digest(source.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        StringBuffer result = new StringBuffer();
        char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        for (int idx = 0; idx < bytes.length; ++idx) {
            byte b = bytes[idx];
            result.append(digits[(b & 0xf0) >> 4]);
            result.append(digits[b & 0x0f]);
        }
        return result.toString();
    }
