    static String getDigestedValue(String type, byte[] data, String charset) {
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            return getHexString(digest.digest(data), charset);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
