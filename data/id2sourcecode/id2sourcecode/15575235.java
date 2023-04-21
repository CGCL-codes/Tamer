    public static String calculateKey(Prescription p) throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException {
        MessageDigest md = MessageDigest.getInstance(p.getAlgorithm());
        byte[] digest = md.digest(Prescription.convertToBytes(p));
        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        String val = encoder.encode(digest);
        return val;
    }
