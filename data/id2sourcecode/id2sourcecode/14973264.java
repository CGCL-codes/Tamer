    public void parse(final InputSource input) throws IOException, SAXException {
        if (contentHandler == null) {
            throw new SAXException("No content contentHandler");
        }
        final String rootElement = "fontmap";
        final BufferedReader bufferedReader = new BufferedReader(input.getCharacterStream());
        contentHandler.startDocument();
        final AttributesImpl atts = new AttributesImpl();
        atts.addAttribute(nsu, Constants.EMPTY_STRING, "name", "ID", bufferedReader.readLine());
        atts.addAttribute(nsu, Constants.EMPTY_STRING, "version", "CDATA", bufferedReader.readLine());
        contentHandler.startElement(nsu, rootElement, rootElement, atts);
        newLine();
        contentHandler.startElement(nsu, "font", "font", EMPTY_ATTRIBUTES);
        newLine();
        writeElement(nsu, "font_from", "font_from", getFontAttributes(bufferedReader.readLine()));
        newLine();
        writeElement(nsu, "font_to", "font_to", getFontAttributes(bufferedReader.readLine()));
        newLine();
        contentHandler.endElement(nsu, "font", "font");
        newLine();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            writeFontEntry(line);
            newLine();
        }
        contentHandler.endElement(nsu, rootElement, rootElement);
        newLine();
        contentHandler.endDocument();
    }
