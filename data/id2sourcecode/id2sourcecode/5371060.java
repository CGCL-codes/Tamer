    public static byte[] digest(byte[] b_value) throws Exception {
        return Thumbprint.digest(b_value, ALGORITHM);
    }
