    public static String digest(String data) throws NoSuchAlgorithmException {
        return Digester.digest(data, Algorithm.DEFAULT);
    }
