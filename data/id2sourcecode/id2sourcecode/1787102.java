    public static String generateHash(String value) {
        final String hashingAlgorithm = "SHA-1";
        final MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(hashingAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Unexpected exception! Unknown hashing algorithm <" + hashingAlgorithm + ">. ", e);
        }
        final Charset charset = Charset.forName("ISO-8859-1");
        final byte[] hashInBytes = digest.digest(value.getBytes(charset));
        final StringBuffer buf = new StringBuffer();
        for (int i = 0; i < hashInBytes.length; i++) {
            int halfByte = (hashInBytes[i] >>> 4) & 0x0F;
            int twoHalfs = 0;
            do {
                if ((0 <= halfByte) && (halfByte <= 9)) {
                    buf.append((char) ('0' + halfByte));
                } else {
                    buf.append((char) ('a' + (halfByte - 10)));
                }
                halfByte = hashInBytes[i] & 0x0F;
            } while (twoHalfs++ < 1);
        }
        return buf.toString();
    }
