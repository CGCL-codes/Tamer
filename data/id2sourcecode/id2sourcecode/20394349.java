    @SuppressWarnings("empty-statement")
    public static byte[] computeDigest(String input) throws NoSuchAlgorithmException, IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        DigestInputStream digestInputStream = new DigestInputStream(inputStream, messageDigest);
        byte[] buffer = new byte[4242];
        while (digestInputStream.read(buffer) != -1) ;
        return messageDigest.digest();
    }
