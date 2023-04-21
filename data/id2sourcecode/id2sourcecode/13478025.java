    public static String hash(byte[] val) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA");
        DigestInputStream digestIn = new DigestInputStream(new ByteArrayInputStream(val), md);
        while (digestIn.read() != -1) ;
        byte[] digest = md.digest();
        return "{SHA}" + new String(Base64.encode(digest));
    }
