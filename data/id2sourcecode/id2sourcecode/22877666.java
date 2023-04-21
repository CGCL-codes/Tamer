    public static String getCipherText(String text) {
        String getCipherText = null;
        if ((text != null) && (text.length() > 0)) {
            try {
                MessageDigest aDigest = MessageDigest.getInstance(MESSAGE_DIGEST_NAME);
                if (aDigest != null) {
                    byte[] someBytes = text.getBytes(CHARACTER_ENCODING_NAME);
                    byte[] someCipherBytes = aDigest.digest(someBytes);
                    byte[] someCipherCharactersBytes = Base64.encodeBase64(someCipherBytes);
                    getCipherText = new String(someCipherCharactersBytes, "US-ASCII");
                }
            } catch (Exception anException) {
                LOG.warn(null, anException);
            }
        }
        return getCipherText;
    }
