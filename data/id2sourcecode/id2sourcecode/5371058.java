    public static String digest(String value) throws Exception {
        return Thumbprint.digest(value, ALGORITHM);
    }
