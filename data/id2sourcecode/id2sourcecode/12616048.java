    public static String stringToSHA(String buffer) {
        try {
            MessageDigest shaDigest = MessageDigest.getInstance(Constants.ALGORITHM_SHA);
            byte[] mybytes = buffer.getBytes();
            byte[] byteResult = shaDigest.digest(mybytes);
            return bytesToHex(byteResult);
        } catch (NoSuchAlgorithmException shaex) {
            return null;
        }
    }
