    public static byte[] getPlainDigestedValue(String type, byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance(type);
            return digest.digest(data);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
