    public static String calculateMIC(byte[] message, String digest) throws GeneralSecurityException, MessagingException, IOException {
        final Tracer tracer = baseTracer.entering("calculateMIC2(byte[] message, String digest)", new Object[] { "part", digest });
        byte mic[];
        MessageDigest md = MessageDigest.getInstance(digest);
        DigestInputStream digIn = new DigestInputStream(new ByteArrayInputStream(message), md);
        for (byte buf[] = new byte[4096]; digIn.read(buf) >= 0; ) {
        }
        mic = digIn.getMessageDigest().digest();
        digIn.close();
        String micString = new String(Base64.encode(mic));
        micString += ", " + digest;
        tracer.leaving();
        return micString;
    }
