    private static String H(String data) {
        try {
            console.logEntry();
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return toHexString(digest.digest(data.getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            console.error("Failed to instantiate an ");
            return null;
        } finally {
            console.logExit();
        }
    }
