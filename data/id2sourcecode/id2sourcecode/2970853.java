    protected String getStringValue(Reader reader) throws IOException {
        StringWriter swriter = new StringWriter();
        int c;
        while ((c = reader.read()) != -1) swriter.write(c);
        return swriter.getBuffer().toString();
    }
