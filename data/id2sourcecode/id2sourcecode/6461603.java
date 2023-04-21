    private static String hash(String convert, String algorithm) {
        byte[] barray = stringToByteArray(convert);
        String restring = "";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(barray);
            byte[] result = md.digest();
            restring = byteArrayToString(result);
        } catch (NoSuchAlgorithmException nsa) {
            log.error("Can't find algorithm for " + algorithm + " hash.");
        }
        return restring;
    }
