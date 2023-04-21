    public static String stringToMD5(String buffer) {
        try {
            MessageDigest digest1 = MessageDigest.getInstance(ALGORITHM_MD5);
            byte[] mybytes = buffer.getBytes();
            byte[] byteResult = digest1.digest(mybytes);
            String result = bytesToHex(byteResult);
            return result;
        } catch (NoSuchAlgorithmException md5ex) {
        }
        return null;
    }
