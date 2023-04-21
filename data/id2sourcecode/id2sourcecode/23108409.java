    public static String stringToSHA(String buffer) {
        try {
            MessageDigest shaDigest = MessageDigest.getInstance(ALGORITHM_SHA);
            byte[] mybytes = buffer.getBytes();
            byte[] byteResult = shaDigest.digest(mybytes);
            String result = bytesToHex(byteResult);
            return result;
        } catch (NoSuchAlgorithmException md5ex) {
        }
        return null;
    }
