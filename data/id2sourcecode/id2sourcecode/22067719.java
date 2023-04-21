    public void format(Reader reader, Writer writer) {
        try {
            format(reader, writer, false);
        } catch (WikiParserException e) {
            throw new WikiException("Html formating failed", e);
        }
    }
