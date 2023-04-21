    public static String generateOneWayHash(String input, String knownSalt) {
        try {
            String onewayHash = null;
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            String randomSalt = Utilities.generateUID();
            byte[] hashBytes = digest.digest((knownSalt + randomSalt).getBytes());
            onewayHash = Utilities.encodeBinaryData(hashBytes);
            return onewayHash;
        } catch (Exception e) {
            throw new SystemException(e.getMessage(), e);
        }
    }
