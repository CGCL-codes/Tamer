    public static String calculateMIC(MimeBodyPart part, String digest, boolean includeHeaders) throws GeneralSecurityException, MessagingException, IOException, CryptException, CloneNotSupportedException {
        final Tracer tracer = baseTracer.entering("calculateMIC(MimeBodyPart part, String digest, boolean includeHeaders)", new Object[] { "part", digest, new Boolean(includeHeaders) });
        byte[] mic = null;
        MessageDigest md = MessageDigest.getInstance(digest.toUpperCase());
        md = (MessageDigest) md.clone();
        tracer.info("MessageDigest object has been created.");
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        byte[] data = null;
        if (includeHeaders) {
            data = Transformer.convertInputStreamToByteArray(part.getDataHandler().getDataSource().getInputStream());
            tracer.info("Part has been converted to byte array.");
        } else {
            Util.copy(part.getInputStream(), bOut);
            tracer.info("Part object has been copied to output stream.");
            data = bOut.toByteArray();
        }
        InputStream bIn = Util.trimCRLFPrefix(data);
        tracer.info("CRLFPrefix has been dropped.");
        DigestInputStream digIn = new DigestInputStream(bIn, md);
        for (byte[] buf = new byte[4096]; digIn.read(buf) >= 0; ) ;
        bOut.close();
        mic = digIn.getMessageDigest().digest();
        String micString = new String(Base64.encode(mic));
        StringBuffer micResult = new StringBuffer(micString);
        micResult.append(", ").append(digest);
        tracer.leaving();
        return micResult.toString();
    }
