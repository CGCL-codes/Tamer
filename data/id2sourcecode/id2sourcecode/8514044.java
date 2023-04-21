    public static String parse(String input, ParserHandler handler) throws StasisException {
        StringReader reader = new StringReader(input);
        StringWriter writer = new StringWriter();
        try {
            parse(reader, writer, handler);
        } catch (IOException e) {
            throw new StasisException("IO error while parsing", e);
        }
        return writer.toString();
    }
