    private void writeArray(StartElement start, XMLEventReader reader, PrintWriter writer) throws XMLStreamException, UnsupportedEncodingException, CoreException, IOException, TransformException, SaxonApiException {
        indent(writer);
        writer.print("[");
        boolean bFirst = true;
        do {
            if (!parse(reader, writer, !bFirst)) break;
            bFirst = false;
        } while (true);
        writer.print("]");
    }
