    public static byte[] getMessageDigest(byte[] input) {
        byte[] digestValue = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(input);
            digestValue = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            log.error("No message digest algorithm specified error " + e.getMessage());
        }
        return digestValue;
    }
