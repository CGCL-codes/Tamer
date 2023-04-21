    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        System.out.println(Hex.encodeHex(MessageDigest.getInstance("MD5").digest("plop2".getBytes("UTF-8"))));
    }
