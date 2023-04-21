    public static String computeChecksum(String name, int units, Date date, int index, String secret) throws AceException {
        String date_s = null;
        if (date != null) {
            date_s = date.toString();
        } else {
            date_s = "null";
        }
        String str = secret + name + index + units + date_s + secret;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] output = md.digest(str.getBytes());
            return Encryption.byteToString(output);
        } catch (NoSuchAlgorithmException ex) {
            throw new AceException("Checksum compute failed : " + ex.getMessage());
        }
    }
