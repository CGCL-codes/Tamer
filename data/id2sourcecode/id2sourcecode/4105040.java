    public void write(BufferedReader reader, BufferedWriter writer) throws IOException {
        OBOFormatParser parser = new OBOFormatParser();
        OBODoc doc = parser.parse(reader);
        write(doc, writer);
    }
