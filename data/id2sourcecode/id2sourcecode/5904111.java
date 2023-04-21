    public static String calculateHash(String data, String algorithm, String charset) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] databytes = data.getBytes(charset);
        md.update(databytes);
        byte[] hashBytes = md.digest();
        return byteArray2HexaDecimal(hashBytes);
    }
