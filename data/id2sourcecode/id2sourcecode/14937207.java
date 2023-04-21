    static String getDigestedValue(String type, byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            return getHexString(digest.digest(data));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
