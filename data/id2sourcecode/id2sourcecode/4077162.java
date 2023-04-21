    private String removeWhitespaces(Reader reader) {
        StringWriter writer = new StringWriter();
        transform(templates, reader, writer);
        StringReader stringReader = new StringReader(writer.toString());
        StringBuilder builder = new StringBuilder();
        try {
            int i;
            while ((i = stringReader.read()) != -1) {
                char c = (char) i;
                if ((c != ' ' && c != '\t' && c != '\r' && c != '\n' && c != '\f')) {
                    builder.append((char) c);
                }
            }
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
        return builder.toString();
    }
