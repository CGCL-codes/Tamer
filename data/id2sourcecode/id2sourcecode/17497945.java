    public String format(String input) {
        Reader reader = new StringReader(input);
        Writer writer = new StringWriter();
        this.format(reader, writer);
        return writer.toString();
    }
