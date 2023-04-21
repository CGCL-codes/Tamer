    public static byte[] readFile(String asFileName) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        InputStream file = Utilities.class.getResourceAsStream(asFileName);
        int i;
        while ((i = file.read()) != -1) stream.write((byte) i);
        return stream.toByteArray();
    }
