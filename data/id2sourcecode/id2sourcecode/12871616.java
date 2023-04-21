    private void writeString(StartElement start, XMLEventReader reader, PrintWriter writer) throws XMLStreamException, UnsupportedEncodingException, FileNotFoundException, IOException, TransformException, SaxonApiException, CoreException {
        String value = getAttr(start, kATTR_VALUE);
        String src = getAttr(start, kATTR_SRC);
        String encoding = getAttr(start, kATTR_ENCODING);
        String unwrap = getAttr(start, kATTR_UNWRAP);
        String html = getAttr(start, kATTR_HTML);
        boolean bReadToEnd = true;
        String chars;
        if (value != null) chars = value; else if (src != null) chars = readFile(src, encoding); else {
            bReadToEnd = false;
            chars = readString(reader, Util.parseBoolean(html));
        }
        if (Util.parseBoolean(unwrap)) {
            value = unwrap(value);
        }
        writer.print(JSONObject.quote(chars));
        if (bReadToEnd) readToEnd(reader);
    }
