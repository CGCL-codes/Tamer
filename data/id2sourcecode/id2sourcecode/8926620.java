    public static String get(byte[] stringBytes, String algoString) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance(algoString);
            algorithm.reset();
            algorithm.update(stringBytes);
            byte messageDigest[] = algorithm.digest();
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException ex) {
        }
        return null;
    }
