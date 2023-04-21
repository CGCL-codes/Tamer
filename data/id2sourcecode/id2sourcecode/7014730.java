    public static String toMd5(String string) throws InternalCnvException {
        String encrypted;
        if (string == null || string.equals("")) {
            throw new InternalCnvException("Missing non-empty string to call toMd5() on.");
        } else {
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(string.getBytes());
                BigInteger number = new BigInteger(1, messageDigest);
                encrypted = number.toString(16);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }
        return encrypted;
    }
