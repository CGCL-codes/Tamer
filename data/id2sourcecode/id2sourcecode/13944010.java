    public static String generateSha1(byte[] input) throws Exception {
        System.out.println("input byte length: " + input.length);
        MessageDigest hash = MessageDigest.getInstance("SHA1");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
        DigestInputStream digestInputStream = new DigestInputStream(byteArrayInputStream, hash);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int ch;
        while ((ch = digestInputStream.read()) >= 0) {
            System.out.println((char) ch);
            byteArrayOutputStream.write(ch);
        }
        byte[] newInput = byteArrayOutputStream.toByteArray();
        System.out.println("in digest : " + new String(digestInputStream.getMessageDigest().digest()));
        byteArrayOutputStream = new ByteArrayOutputStream();
        DigestOutputStream digestOutputStream = new DigestOutputStream(byteArrayOutputStream, hash);
        digestOutputStream.write(newInput);
        digestOutputStream.close();
        String sha1 = new String(digestOutputStream.getMessageDigest().digest());
        System.out.println("out digest: " + sha1);
        return sha1;
    }
