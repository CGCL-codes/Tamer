    private void transform(ElementReader reader, Writer writer) throws IOException {
        reader.setStyle(style);
        Element element = reader.read();
        new ElementWriter(writer).write(element);
    }
