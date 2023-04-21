    public static String getSha1Hash(String data) {
        try {
            StringDigest = MessageDigest.getInstance("SHA-1");
            DigestBytes = StringDigest.digest(data.getBytes());
            DigestNumber = new BigInteger(1, DigestBytes);
            final StringBuilder hashText = new StringBuilder();
            hashText.append(DigestNumber.toString(HASH_LIMIT));
            while (hashText.length() < HASH_LENGTH) {
                hashText.append("0" + hashText);
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            if (!SystemLog.canLogMessage(e.getMessage(), Level.SEVERE)) {
                e.printStackTrace();
            }
        }
        return null;
    }
