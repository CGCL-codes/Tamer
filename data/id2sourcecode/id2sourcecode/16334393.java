    public static String convert(String html) throws ParseException {
        Reader reader = new StringReader(html);
        StringWriter writer = new StringWriter();
        try {
            ConversionContext context = new ConversionContext(reader, writer, "UTF-8");
            convert(context);
            return writer.getBuffer().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtil.close(reader);
        }
    }
