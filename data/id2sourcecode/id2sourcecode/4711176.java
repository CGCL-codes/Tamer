    protected static boolean testStreamsV2(Base64Decoder decoder, byte[] original, int test) throws CodingException, IOException {
        System.out.println("Original [length=" + original.length + "]");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        Base64OutputStream b64Output = new Base64OutputStream(output, decoder);
        b64Output.write(original);
        b64Output.close();
        byte[] encoded = output.toByteArray();
        System.out.println("Encoded [length=" + encoded.length + "]:\r\n" + new String(encoded));
        output = new ByteArrayOutputStream();
        Base64InputStream b64Input = new Base64InputStream(new ByteArrayInputStream(encoded), decoder);
        int b;
        while ((b = b64Input.read()) != -1) output.write(b);
        byte[] decoded = output.toByteArray();
        System.out.println("Decoded [length=" + decoded.length + "]");
        boolean passed = Arrays.equals(original, decoded);
        System.out.println("Test[" + test + "] " + (passed ? "passed................................................................." : "failed<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<"));
        return passed;
    }
