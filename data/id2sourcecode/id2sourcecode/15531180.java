    public static String digest(byte[] input, String algoritmo) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algoritmo);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        md.reset();
        return asHex(md.digest(input));
    }
