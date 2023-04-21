    public static byte[] toByteArray(final Reader reader) throws IOException {
        Assert.notNull(reader, "reader");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);
        IOUtils.copy(reader, writer);
        return outputStream.toByteArray();
    }
