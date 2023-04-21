    static String signPassword(char[] password) {
        try {
            char[] hashedPassword = hashPassword(password);
            StringBuilder sb = new StringBuilder(String.valueOf(hashedPassword));
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            sb.append(String.format("%032x", new BigInteger(1, messageDigest.digest(String.valueOf(hashedPassword).getBytes()))));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
