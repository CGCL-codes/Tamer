    static char[] unsignPassword(String text) throws InvalidPasswordException {
        try {
            String password = text.substring(0, text.length() - 32);
            String digest = text.substring(text.length() - 32);
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            if (!new BigInteger(1, messageDigest.digest(password.getBytes())).equals(new BigInteger(digest, 16))) {
                throw new InvalidPasswordException("Password is invalid");
            }
            return hashPassword(password.toCharArray());
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }
