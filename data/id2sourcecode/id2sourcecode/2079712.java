    public static String encrypt(final String s) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            final byte[] messageDigest = md.digest(s.getBytes());
            final BigInteger number = new BigInteger(1, messageDigest);
            return number.toString(16);
        } catch (final NoSuchAlgorithmException e) {
            System.out.println("Couldn't find MD5 algorithm");
        }
        return null;
    }
