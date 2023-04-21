    public static byte[] digest(byte data[]) throws NoSuchAlgorithmException, NoSuchProviderException {
        MessageDigest digester;
        digester = MessageDigest.getInstance("SHA-1", "BC");
        digester.update(data);
        return digester.digest();
    }
