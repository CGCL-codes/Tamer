    public TaggedSource parse(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        int ch;
        while ((ch = reader.read()) != -1) writer.write(ch);
        reader.close();
        return parse(writer.toString());
    }
