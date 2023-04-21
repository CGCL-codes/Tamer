    public static void convert(Reader reader, OutputStream out, String encoding) throws ParseException, UnsupportedEncodingException {
        Writer writer = new OutputStreamWriter(out, encoding);
        try {
            ConversionContext context = new ConversionContext(reader, writer, encoding);
            convert(context);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(reader);
            IOUtil.close(writer);
        }
    }
