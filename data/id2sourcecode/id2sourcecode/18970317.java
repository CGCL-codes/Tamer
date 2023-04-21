    public static String calcolaMD5(byte[] stream) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] b = messageDigest.digest(stream);
            String md5 = HexReader.bytesToHex(b);
            return md5;
        } catch (NoSuchAlgorithmException e) {
            return "errore md5 " + e.getMessage();
        }
    }
