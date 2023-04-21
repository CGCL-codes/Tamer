    private void writeMember(StartElement start, XMLEventReader reader, PrintWriter writer) throws XMLStreamException, UnsupportedEncodingException, CoreException, IOException, TransformException, SaxonApiException {
        indent(writer);
        String name = start.getAttributeByName(new QName("name")).getValue();
        writer.print(JSONObject.quote(name));
        writer.print(":");
        if (parse(reader, writer, false)) readToEnd(reader);
    }
